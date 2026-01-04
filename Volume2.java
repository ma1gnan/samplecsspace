import java.util.Scanner;

/**
   This program prints the price per ounce for a six-pack of cans.
*/
public class Volume2
{
   public static void main(String[] args)
   {
      // Read price per pack

      Scanner in = new Scanner(System.in);

      System.out.print("Please enter the price for a six-pack: ");
      double packPrice = in.nextDouble();

      // Read can volume

      System.out.print("Please enter the volume for each can (in ounces): ");
      double canVolume = in.nextDouble();

      // Compute pack volume 
   
      final double CANS_PER_PACK = 6;
      double packVolume = canVolume * CANS_PER_PACK;

      // Compute and print price per ounce

      double pricePerOunce = packPrice / packVolume;

      System.out.printf("Price per ounce: %8.2f", pricePerOunce);
      System.out.println();
   }
}
