/**
  @file value.h
  @author Nikolaus Johnson (njohnso5)

  Support for representing values and for keeping up with the current
  value of all variables.
*/

#include <stdio.h>
#include <stdbool.h>

#ifndef _VALUE_H_
#define _VALUE_H_
/** Number to multiply by to get the new capacity */
#define CAPACITY_DOUBLER 2
/** Initial capacity for environment */
#define INIT_CAP 5

//////////////////////////////////////////////////////////////////////
// Value, an interface for different types of values supported by our
// language.

/** A short name to use for the Value interface. */
typedef struct ValueStruct Value;

/** Abstract representation for a Value computed in our programming
    language.  Subclasses implementing this interface must have these
    four fields as their first members.  They will need to set them
    to point to appropriate functions based on the kind of value
    they represent.
*/
struct ValueStruct {
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
};

/** Make a subclass of value representing the given integer value. 
    @param val The value this new integer should have.
    @return Pointer to a new, dynamically allocated value.
 */
Value *makeInteger( int val );

/** Lots of operations require integer parameters.  This function
    makes it easy to get a value as an int.  It exits with an error
    message if the given value isn't an Integer.  If it is, it frees
    the value (since, typically, will no longer need it)
    and returns a copy of the integer inside it.
    @param Value that we want to convert to an integer.
    @return integer value it converts to.
*/
int toInt( Value *value );
  
//////////////////////////////////////////////////////////////////////
// Environment, a mapping from variables names to their current.

// Maximum length of an identifier (variable) name.
#define MAX_VAR_NAME 20

/**
   Short typename for the Environment structure.  Its definition is an
   implementation detail of the language, not visible to client code.
*/
typedef struct EnvironmentStruct Environment;


/** Create and return a new, empty environment object.
    @return new, dynamically allocated environment object.  The caller
    must eventually free this with freeEnvironemtn().
*/
Environment *makeEnvironment();


/** Lookup the variable with the given name in the environment and
    return its value.  If the variable doesn't have a value, this
    function returns null.
    @param env Environment object in which to lookup the variable.
    @param name requested variable name.
    @return the variable's value.  This is a pointer into the
    environment's representation and should not be directly freed or
    modified by the caller.
*/
Value *lookupVariable( Environment *env, char const *name );

/** In the given environment, set the named variable to store the given
    value.

    @param env Environment in which to store the variable name / value.
    @param name of the variable to set the value for.
    @param value new value for this variable.
*/
void setVariable( Environment *env, char const *name, Value *value );

/** Free all the memory associated with this environment.
    @param env environment to free memory for.
*/
void freeEnvironment( Environment *env );

#endif
