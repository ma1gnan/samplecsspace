#include<stdio.h>
#include<stdlib.h>
#include<time.h>

int main()
{
	int my_arr[10];
	int i;

	srand(time(0));

	printf("Enter ten integers: ");
	// Get user input store in array	
	for(i=0; i<10; i++)
	{
		scanf("%d", &my_arr[i]);
	}


	// Generate random number in range 0-10
	// Use random number as indicies to update array value
	for(i=0; i<100; i++)
	{
		int random_num = rand() % 10;
		my_arr[random_num] += 1;
	}


	// Print out updated array
	printf("\nUpdated Array:  ");
	for(i=0; i<10; i++)
	{
		printf("%d  ", my_arr[i]);
	}

	printf("\n");
	return 0;
}
