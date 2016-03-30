#include <termios.h>
#include <stdio.h>
#include <conio.h>
int main(void) {
  char c;
  printf("(getche example) please type a letter: ");
  c = getche();
  printf("\nYou typed: %c\n", c);
  printf("(getch example) please type a letter...");
  c = getch();
  printf("\nYou typed: %c\n", c);
  return 0;
} 
