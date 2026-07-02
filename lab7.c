#include<stdio.h>

// LAB 7 a program that asks the user to enter a series of integers
// then sorts the integers by calling the function selection_sort
void selection_sort(int a[], int n);

int main() {
	int amount;

	printf("How many integers: ");
	scanf("%d", &amount);

	int userInput[amount];

	//getting user input
	printf("Enter %d integers: ", amount);
	for(int i=0; i<amount;i++){
		scanf("%d", &userInput[i]);
	}
	printf("\n");
	selection_sort(userInput, amount);

	//print sorted array
	printf("\nSorted Array:  ");
	for(int i=0; i<amount; i++){
		printf("%d  ", userInput[i]);
	}
	printf("\n");	
	return 0;
}
// the function selection_sort searches the array to find the largest element
// then moves it to the back
// call itself recursively to sort the first n-1 element of the array
void selection_sort(int a[], int n){

	if (n<=1)
		return;

	int maxNum = 0;
	for(int i=1; i<n; i++){
		if (a[i] > a[maxNum])
			maxNum = i;
	}
	
	int temp = a[maxNum];
	a[maxNum] = a[n-1];
	a[n-1] = temp;
	
	printf("After the next call: ");
	for(int j=0; j<n-1; j++){
		printf("%d ", a[j]);
	}
	printf("\n");
	selection_sort(a, n-1);	
}

