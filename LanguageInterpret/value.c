/**
   @file value.c
   @author Nikolaus Johnson
   This file contains the value subclass Integer and all its function implementations, as well as the Environment struct for holding variables along with all its function implementations.
*/
#include "value.h"
#include <stdlib.h>
#include <string.h>

//////////////////////////////////////////////////////////////////////
// Value Subclasses

/** Representation for an Integer, a subclass of Value. */
typedef struct {
  void (*print)( Value *value, FILE *stream );
  bool (*equals)( Value *value, Value *other );
  Value *(*copy)( Value *value );
  void (*destroy)( Value *value );

  /** Integer value contained in this Value subclass. */
  int val;
} Integer;

/** Implementation of print for Integer */
static void printInteger( Value *value, FILE *stream )
{
  // If this function gets called, value must really be an Integer.
  Integer *this = (Integer *) value;
  printf( "%d", this->val );
}

/** Implementation of equals for Integer */
static bool equalsInteger( Value *value, Value *other )
{
  // If this function gets called, value must really be an Integer.
  Integer *this = (Integer *) value;
  
  // If they're not the same type, then theyre not equal. We can use
  // one of the function poitners to make sure other is an Integer.  It is
  // if it has the right print function for an int.
  if ( other->print != printInteger )
    return false;

  // If they're both integers, return true if they contain the same value.
  Integer *that = (Integer *) other;
  return this->val == that->val;
}

/** Implementation of copy for Integer */
static Value *copyInteger( Value *value )
{
  return makeInteger( ((Integer *) value)->val );
}

/** Implementation of destroy for Integer */
static void destroyInteger( Value *value )
{
  free( value );
}

Value *makeInteger( int val )
{
  // Allocate space for an Integer object.
  Integer *this = (Integer *) malloc( sizeof( Integer ) );

  // Record pointers to the right functions for working with this object.
  this->print = printInteger;
  this->equals = equalsInteger;
  this->copy = copyInteger;
  this->destroy = destroyInteger;

  // Record a copy of the given integer value.
  this->val = val;

  return (Value *) this;
}

/** Documented in the header. */
int toInt( Value *value )
{
  // We can use one of the function poitners to make sure this is an
  // Integer.
  if ( value->print != printInteger ) {
    fprintf( stderr, "Expected Integer value\n" );
    exit( EXIT_FAILURE );
  }

  // Get a copy of the value, then free the value that contained it.
  int val = ((Integer *) value)->val;
  value->destroy( value );

  // Return the int value that was inside.
  return val;
}

//////////////////////////////////////////////////////////////////////
// Environment.

// Define your own Environment struct to hold the values of all
// variables.
/**
   This structure is used for the environment that holds all variables and 
   their values.  The structure includes a cap on how large its pointers are, 
   as well as a count of how many variables are defined.  
   This implementation also uses a lastObtained variable for keeping track 
   of the last variable looked up
*/
struct EnvironmentStruct {
  /** Dynamically allocating capacity for pointers */
  int cap;
  /** This variable is used to keep track of how many variables are in the
      environment */
  int count;
  /** This variable is used to track variables obtained from the environment 
      lists 
  */
  int lastObtained;
  /** This pointer points to a number of character pointers that state the names
      of the variables */
  char **varNames;
  /** This pointer points to a number of value pointers that state the values of
      the variables */
  Value **values;
};
/** Documentation in the header */
Environment *makeEnvironment()
{
  Environment *env = (Environment *)malloc(sizeof(Environment));
  env->cap = INIT_CAP;
  env->count = 0;
  env->lastObtained = -1;
  env->varNames = (char **)malloc(env->cap * sizeof(char *));
  for (int i = 0; i < env->cap; i++) {
    env->varNames[i] = (char *)malloc((MAX_VAR_NAME + 1) * sizeof(char));
  }
  env->values = (Value **)malloc(env->cap * sizeof(Value *));
  return env;
}
/** Documentation in the header */
Value *lookupVariable( Environment *env, char const *name )
{
  for (int i = 0; i < env->count; i++) {
    if (strcmp(env->varNames[i], name) == 0) {
      env->lastObtained = i;
      return env->values[i];
    }
  }
  env->lastObtained = -1;
  return NULL;
}
/** Documentation in the header */
void setVariable( Environment *env, char const *name, Value *value )
{
  if (strcmp(name, "if") != 0 && strcmp(name, "while") != 0 && strcmp(name, "print") != 0) {
    if (lookupVariable(env, name)) {
      env->values[env->lastObtained]->destroy(env->values[env->lastObtained]);
      env->values[env->lastObtained] = value;
    }
    else {
      if (env->count >= env->cap) {
        env->cap *= CAPACITY_DOUBLER;
        env->varNames = (char **)realloc(env->varNames, env->cap * sizeof(char *));
        for (int i = env->count; i < env->cap; i++) {
          env->varNames[i] = (char *)malloc((MAX_VAR_NAME + 1) * sizeof(char));
        }
        env->values = (Value **)realloc(env->values, env->cap * sizeof(Value *));
      }
      strncpy(env->varNames[env->count], name, MAX_VAR_NAME);
      env->varNames[env->count][MAX_VAR_NAME] = '\0';
      env->values[env->count] = value;
      env->count++;
    }
  }
}
/** Documentation in the header */
void freeEnvironment( Environment *env )
{
  for (int i = 0; i < env->cap; i++) {
    free(env->varNames[i]);
  }
  free(env->varNames);
  for (int i = 0; i < env->count; i++) {
    env->values[i]->destroy(env->values[i]);
  }
  free(env->values);
  free(env);
}

