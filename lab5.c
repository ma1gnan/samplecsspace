//lab 5 code using for loop to make a month calander
#include <stdio.h>

int main()
{
	int day;
	int start;
	int empty_days;
	//request number of days for the month save in variable day 
	printf("Enter number of days in month: ");
	scanf("%d", &day);

	//request the start day and save in the variable start
	printf("Enter start day of the week (1=Sun, 7=Sat): ");
	scanf("%d", &start);
	empty_days = start - 1;
	printf("Your requested month's calander is:\n");

	// for loop for empty space
	for(int j=1; j<=empty_days; j++)
	{
		printf("    ");
	}
	//use a for loop to make the calander 
	for(int i=1; i<=day; i++)
	{
		printf("%3d ",i);
		if ((empty_days + i) %7 == 0)
		{
			printf("\n");
		}
	}
	printf("\n");
	return 0;
}
