from pyspark.sql import SparkSession
from pyspark.sql.functions import count

def main():
    # Create Spark session
    spark = SparkSession.builder \
        .appName("PurchaseCount") \
        .getOrCreate()

    # Read the CSV file
    df = spark.read.csv(
        "Purchase.csv",
        header=True,
        inferSchema=True
    )

    # Count the number of purchases made by each person
    purchase_count = (
        df.groupBy("Name")
          .agg(count("*").alias("Purchase_Count"))
          .orderBy("Name")
    )

    # Display the result
    purchase_count.show()

    # Optional: Save the output
    purchase_count.write.mode("overwrite").csv(
        "Purchase_Count_Output",
        header=True
    )

    spark.stop()


if __name__ == "__main__":
    main()
