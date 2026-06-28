from pyspark.sql import SparkSession
from pyspark.sql.functions import count

import os
import sys

# FORCE Python to use the Homebrew Java 17 executable path
os.environ["JAVA_HOME"] = "/opt/homebrew/opt/openjdk@17"
sys.path.append("/opt/homebrew/opt/openjdk@17/bin")

# Now import pyspark safely
from pyspark.sql import SparkSession

def main():
    # Clean SparkSession setup
    spark = SparkSession.builder \
        .appName("Count Purchases") \
        .getOrCreate()

    print("Spark initialized successfully using Java 17!")
    

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


    spark.stop()


if __name__ == "__main__":
    main()