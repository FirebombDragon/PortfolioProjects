#include "stringval.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

/** Implementation of copy for String */
static Value *copyString( Value *value )
{
  return makeString( ((String *) value)->val );
}

/** Implementation of print for String */
static void printString( Value *value, FILE *stream )
{
  // If this function gets called, value must really be an Integer.
  String *this = (String *) value;
  printf( "%s", this->val );
}

/** Implementation of equals for String */
static bool equalsString( Value *value, Value *other )
{
  // If this function gets called, value must really be an Integer.
  String *this = (String *) value;
  
  // If they're not the same type, then theyre not equal. We can use
  // one of the function poitners to make sure other is a String.  It is
  // if it has the right print function for a String.
  if ( other->print != printString )
    return false;

  // If they're both strings, return true if they contain the same value.
  String *that = (String *) other;
  return (strcmp(this->val, that->val) == 0);
}

/** Implementation of destroy for String */
static void destroyString( Value *value )
{
  String *this = (String *) value;
  free( this->val );
  free( this );
}
/** Documented in the header */
Value *makeString( char *val )
{
  // Allocate space for a String object.
  String *this = (String *) malloc( sizeof( String ) );

  // Record pointers to the right functions for working with this object.
  this->print = printString;
  this->equals = equalsString;
  this->copy = copyString;
  this->destroy = destroyString;
  // Record a copy of the given string value.
  if (val) {
    char *temp = val;
    int num = 0;
    while (temp[0] && temp[0] != '\0') {
      num++;
      temp = temp + 1;
    }
    this->val = (char *) malloc(num + 1);
    for (int i = 0; i < num + 1; i++) {
      this->val[i] = val[i];
    }
    //this->val[strlen(this->val)] = '\0';
    //strcat(this->val, "\0");
  }
  return (Value *) this;
}

/** Documented in the header. */
char *toString( Value *value )
{
  // We can use one of the function poitners to make sure this is a
  // String.
  if ( value->print != printString ) {
    fprintf( stderr, "Expected String value\n" );
    exit( EXIT_FAILURE );
  }

  // Get a copy of the value, then free the value that contained it.
  char *val = ((String *) value)->val;
  value->destroy( value );

  // Return the string value that was inside.
  return val;
}
