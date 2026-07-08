from pyspark.sql import SparkSession
from pyspark.ml.clustering import KMeans
import matplotlib.pyplot as plt

#Create Spark session
spark = SparkSession.builder \
    .appName("KMeansExample") \
    .getOrCreate()

# Read the dataset
dataset = spark.read.format("libsvm").load("kmeans_input.txt")

# Display the input data
print("Input Datset:")
dataset.show(10, truncate=False)


# Create KMeans model
kmeans = KMeans(
    k=2,
    seed=1,
    featuresCol="features",
    predictionCol="cluster"
)

# Train the model
model = kmeans.fit(dataset)

# Predict cluster label
predictions = model.transform(dataset)

# Display clustering result
print("Cluster Assignments:")
predictions.groupBy("cluster").count().show()

# Collect predictions from Spark, separate points by cluster
points = predictions.select("features", "cluster").collect()

cluster0_x = []
cluster0_y = []
cluster1_x = []
cluster1_y = []

for row in points:
    x = row.features[0]
    y = row.features[1]

    if row.cluster == 0:
        cluster0_x.append(x)
        cluster0_y.append(y)
    else:
        cluster1_x.append(x)
        cluster1_y.append(y)

# Create the figure for cluster 0 and 1
plt.figure(figsize=(5,5))

plt.plot(
    cluster0_x,
    cluster0_y,
    'o',
    markerfacecolor='white',
    markeredgecolor='black',
    markersize=4,
    linestyle='None',
    label='Cluster 1'
)

plt.plot(
    cluster1_x,
    cluster1_y,
    '+',
    color='black',
    markersize=6,
    linestyle='None',
    label='Cluster 2'
)

# Label and match axis
plt.xlabel('x')
plt.ylabel('y')
plt.xlim(0, 3)
plt.ylim(0, 3)

plt.legend(loc='upper right', frameon=True)

# Display graph
plt.show()

# Print cluster centers
print("Cluster Centers:")
centers = model.clusterCenters()

for i, center in enumerate(centers):
    print("Cluster", i, ":", center)

# Compute With set sum of squared errors
print("Training Cost (Within Set Sum of Squared Errors):")
print(model.summary.trainingCost)

spark.stop()
