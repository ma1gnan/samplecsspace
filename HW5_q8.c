// this program function returns a pointer to the last node that contains n;
// and if there is no n in the list it returns NULL

#include <stdio.h>
#include <stdlib.h>

// define the node structure
struct node
{
    int value;
    struct node *next;
};

// function to find last node with the n value
struct node*find_last(struct node *list, int n)
{
    //This will store the last node that matches n
    struct node *last = NULL; 

    // loop to go throught the whole linked list
    while (list != NULL)
    {
        // finding and storing the node that contains the value n
        if (list->value == n)
        {
            last = list;
        }
        // moving to the next node
        list = list->next;
    }
    // last will point to the last n in the list or NULL
    return last;
}

// funtion to create a new node
struct node *create_node(int value)
{
    struct node *new_node = (struct node *)malloc(sizeof(struct node));
    
    new_node->value = value;
    new_node->next = NULL;
    return new_node;
}

// function to append node to back of the list
void append_node(struct node **head, int value)
{
    struct node *new_node = create_node(value);
    if (*head == NULL)
    {
        *head = new_node;
        return;
    }

    struct node *temp = *head;
    while (temp->next != NULL)
        temp = temp->next;

    temp->next = new_node;
}

int main()
{
    struct node *list = NULL;

    // my list 
    append_node(&list, 3);
    append_node(&list, 6);
    append_node(&list, 6);
    append_node(&list, 9);
    append_node(&list, 7);
    append_node(&list, 5);

    int search_value = 5;
    struct node *last_node = find_last(list, search_value);

    if (last_node != NULL)
        printf("The last node with the %d is at %p\n", search_value, (void *)last_node);
    else
        printf("%d not found!", search_value);

    struct node *temp;
    while(list != NULL)
    {
        temp = list;
        list = list->next;
        free(temp);
    }

    return 0;
}

