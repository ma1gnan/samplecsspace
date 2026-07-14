import argparse
from pyspark.sql import SparkSession
from pyspark.sql.functions import col, desc

# Build a SparkSession
def build_spark_session(app_name: str = "TweetStateCount") -> SparkSession:
    return (
        SparkSession.builder
        .appName(app_name)
        .getOrCreate()
    )

# Load the two JSON files into DataFrames
# Join tweets to the city/state lookup table.
# Group by state and count the number of tweets in each state.
def count_tweets_by_state(spark: SparkSession, tweets_path: str, city_state_path: str):

    tweets_df = spark.read.json(tweets_path)
    city_state_df = spark.read.json(city_state_path)

    joined_df = tweets_df.join(
        city_state_df,
        tweets_df["geo"] == city_state_df["city"],
        how="inner",
    )

    state_counts_df = (
        joined_df
        .groupBy("state")
        .count()
        .orderBy(desc("count"))
    )

    return state_counts_df

# Main function to parse arguments and execute the tweet counting
def main():
    parser = argparse.ArgumentParser(description="Count tweets per state.")
    parser.add_argument("--tweets", default="Tweets.json",
                         help="Path to Tweets.json")
    parser.add_argument("--city-state", default="cityStateMap.json",
                         help="Path to cityStateMap.json")
    args = parser.parse_args()

    spark = build_spark_session()

    result_df = count_tweets_by_state(spark, args.tweets, args.city_state)

    result_df.show(truncate=False)

    spark.stop()


if __name__ == "__main__":
    main()
