/**
  @file stringval.h
  @author Nikolaus Johnson (njohnso5)

  Support for values that contain strings.
*/
#include "value.h"
/** 
    This structure is a subclass of the Value structure with all of the 
    functions associated with the Value structure as well as a character pointer
    for the value of the string.
*/
typedef struct StringValue {
  /** Pointer to a function to print this value to the given stream.
      @param value this pointer for the current value.
      @param stream where the value should be printed.
   */
  void (*print)( Value *value, FILE *stream );

  /** Compare this value to the  given, other value.
      @param value this pointer for the current value.
      @param other value to compare against
      @return true if this value and other have the same type and contents.
  */
  bool (*equals)( Value *value, Value *other );
  
  /** Dynamically allocate a copy of this value.
      @param value this pointer for the current value.
      @return copy of the value.
  */
  Value *(*copy)( Value *value );
  
  /** Free memory for this value.
      @param value this pointer for the current value.
  */
  void (*destroy)( Value *value );
  char *val;
} String;

/** Make a subclass of value representing the given string value. 
    @param val The value this new string should have.
    @return Pointer to a new, dynamically allocated value.
 */
Value *makeString( char *val );

/** Lots of operations require string parameters.  This function
    makes it easy to get a value as an string.  It exits with an error
    message if the given value isn't a String.  If it is, it frees
    the value (since, typically, will no longer need it)
    and returns a copy of the string inside it.
    @param Value that we want to convert to an string.
    @return string value it converts to.
*/
char *toString( Value *value );
