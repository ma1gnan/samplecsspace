import os
import sys

# Force Python to use the Homebrew Java 17 executable path
os.environ["JAVA_HOME"] = "/opt/homebrew/opt/openjdk@17"
sys.path.append("/opt/homebrew/opt/openjdk@17/bin")

from pyspark.sql import SparkSession

def main():
    # Initialize Spark
    spark = SparkSession.builder \
        .appName("Average Age by Gender RDD") \
        .getOrCreate()
        
    sc = spark.sparkContext

    #Load the text file as an RDD
    raw_rdd = sc.textFile("people.csv")

    #Filter out the header row
    header = raw_rdd.first()
    data_rdd = raw_rdd.filter(lambda line: line != header)

    #Parse the lines and map to key-value pairs: (gender, (age, 1))
    #We use a try/except or split to pull gender (index 2) and age (index 1)
    parsed_rdd = data_rdd.map(lambda line: line.split(",")) \
                         .map(lambda cols: (cols[2], (int(cols[1]), 1)))

    #Reduce by key to sum the ages and sum the counts
    # (gender, (total_age, total_count))
    totals_rdd = parsed_rdd.reduceByKey(lambda a, b: (a[0] + b[0], a[1] + b[1]))

    # Map to calculate the average: total_age / total_count
    # (gender, average_age)
    averages_rdd = totals_rdd.mapValues(lambda val: round(val[0] / val[1], 2))

    # Collect and print the final results
    results = averages_rdd.collect()
    
    print("\n--- OUTPUT DATA ---")
    for gender, avg_age in results:
        print(f"Gender: {gender} | Average Age: {avg_age}")
    print("-------------------\n")

    spark.stop()

if __name__ == "__main__":
    main()