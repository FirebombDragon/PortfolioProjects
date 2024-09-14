#include "syntax.h"
#include <stdlib.h>
#include <string.h>

//////////////////////////////////////////////////////////////////////
// Literal

/** Representation for a Literal expression, subclass of Expr. */
typedef struct {
  Value *(*eval)( Expr *expr, Environment *env );
  void (*destroy)( Expr *expr );

  /** Value contained in this literal expression. */
  Value *val;
} LiteralExpr;

/** Implementation of eval for Literal expressions. */
static Value *evalLiteral( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a LiteralExpr.
  LiteralExpr *this = (LiteralExpr *)expr;

  // Return a copy of the value we contain.
  return this->val->copy( this->val );
}

/** Implementation of destroy for Literal expressions. */
static void destroyLiteral( Expr *expr )
{
  // If this function gets called, expr must really be a LiteralExpr.
  LiteralExpr *this = (LiteralExpr *)expr;

  // Free the value we contain and the literal object itself.
  this->val->destroy( this->val );
  free( this );
}

Expr *makeLiteral( Value *val )
{
  // Allocate space for the LiteralExpr object
  LiteralExpr *this = (LiteralExpr *) malloc( sizeof( LiteralExpr ) );

  // Remember the pointers to functions for evaluating and destroying ourself.
  this->eval = evalLiteral;
  this->destroy = destroyLiteral;

  // Remember the literal string we contain.
  this->val = val;

  // Return the result, as an instance of the base.
  return (Expr *) this;
}

//////////////////////////////////////////////////////////////////////
// Infix expressions

/** Representation for an expression with two parameters as
    sub-expressions.  This struct can be used to represent lots of
    expression types. */
typedef struct {
  Value *(*eval)( Expr *expr, Environment *env );
  void (*destroy)( Expr *oper );

  // Two sub-expressions.
  Expr *left, *right;
} InfixExpr;

/** General-purpose function for freeing an expression with two parameters
    as sub-expression. */
static void destroyInfix( Expr *expr )
{
  // If this function gets called, expr must really be a InfixExpr.
  InfixExpr *this = (InfixExpr *)expr;

  // Free our operand subexpressions.
  this->left->destroy( this->left );
  this->right->destroy( this->right );

  // Then the InfixExpr struct itself.
  free( this );
}

/** Helper funciton to construct a InfixExpr representation and fill
    in the parts that are common to all InfixExpr instances. 
    @param left first parameter in the expression. 
    @param right second parameter in the expression. 
    @param eval function implementing the eval function for this expression.
    @return new expression, as a poiner to Expr.
*/
static Expr *buildInfixExpr( Expr *left, Expr *right,
                              Value *(*eval)( Expr *, Environment * ) )
{
  // Allocate space for a new InfixExpr and fill in the pointer for
  // its destroy function.
  InfixExpr *this = (InfixExpr *) malloc( sizeof( InfixExpr ) );
  this->destroy = destroyInfix;

  // Fill in the two parameters and the eval funciton.
  this->eval = eval;
  this->left = left;
  this->right = right;

  return (Expr *) this;
}

/** Implementation of eval for instances of InfixExpr that do addition. */
static Value *evalAdd( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a InfixExpr.
  InfixExpr *this = (InfixExpr *)expr;

  // Get the values our two subexpressions evaluate to and compute the
  // sum.
  Value *v1 = this->left->eval( this->left, env );
  Value *v2 = this->right->eval( this->right, env );

  // This frees the two values we just made and adds the ints inside them all
  // at once.
  int result = toInt( v1 ) + toInt( v2 );

  return makeInteger( result );
}

Expr *makeAdd( Expr *left, Expr *right )
{
  // Use the convenience function to build a InfixExpr for addition
  return buildInfixExpr( left, right, evalAdd );
}

/** Implementation of eval for instances of InfixExpr that do subtraction. */
static Value *evalSub( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a InfixExpr.
  InfixExpr *this = (InfixExpr *)expr;

  // Evaluate our left and right operands and take their difference.  This
  // is a little bit shorter than what we did in evaluating add.
  int v1 = toInt( this->left->eval( this->left, env ) );
  int v2 = toInt( this->right->eval( this->right, env ) );

  return makeInteger( v1 - v2 );
}

Expr *makeSub( Expr *left, Expr *right )
{
  // Use the convenience function to build a InfixExpr for subtraction.
  return buildInfixExpr( left, right, evalSub );
}

/** Implementation of eval for instances of InfixExpr that do multiplication. */
static Value *evalMul( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a InfixExpr.
  InfixExpr *this = (InfixExpr *)expr;

  // Evaluate our left and right operands and compute their product.
  // We can do this all in one statement since toInt() also frees the
  // values resulting fromt he two sub-expressions.
  return makeInteger( toInt( this->left->eval( this->left, env ) ) *
                      toInt( this->right->eval( this->right, env ) ) );
}

Expr *makeMul( Expr *left, Expr *right )
{
  // Use the convenience function to build a InfixExpr for multiplication.
  return buildInfixExpr( left, right, evalMul );
}

/** Implementation of eval for instances of InfixExpr that do division. */
static Value *evalDiv( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a InfixExpr.
  InfixExpr *this = (InfixExpr *)expr;

  // Catch it if we try to divide by zero.
  int d = toInt( this->right->eval( this->right, env ) );
  if ( d == 0 ) {
    fprintf( stderr, "Divide by zero\n" );
    exit( EXIT_FAILURE );
  }

  // Evaluate the first operand and divide by the second.
  return makeInteger( toInt( this->left->eval( this->left, env ) ) / d );
}

Expr *makeDiv( Expr *left, Expr *right )
{
  // Use the convenience function to build a InfixExpr for division.
  return buildInfixExpr( left, right, evalDiv );
}

/** Implementation of eval for instances of InfixExpr that do less-than. */
static Value *evalLess( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a InfixExpr.
  InfixExpr *this = (InfixExpr *)expr;

  // Compare the integer values of our operands and return true or false.
  if ( toInt( this->left->eval( this->left, env ) ) <
       toInt( this->right->eval( this->right, env ) ) )
    return makeInteger( true );
  else
    return makeInteger( false );
}

Expr *makeLess( Expr *left, Expr *right )
{
  // Use the convenience function to build a InfixExpr for the less-than
  // comparison.
  return buildInfixExpr( left, right, evalLess );
}

/** Implementation of eval for instances of InfixExpr that do equality
    comparison. */
static Value *evalEquals( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a InfixExpr.
  InfixExpr *this = (InfixExpr *)expr;

  // Get the values our two subexpressions evaluate to and see if they
  // are equal.
  Value *v1 = this->left->eval( this->left, env );
  Value *v2 = this->right->eval( this->right, env );

  bool result = v1->equals( v1, v2 );
  
  // Free the two subexpressions and return true if they're equals.
  v1->destroy( v1 );
  v2->destroy( v2 );
  
  return result ? makeInteger( true ) : makeInteger( false );
}

Expr *makeEquals( Expr *left, Expr *right )
{
  // Use the convenience function to build a InfixExpr for the equals test.
  return buildInfixExpr( left, right, evalEquals );
}

static Value *evalAnd( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a InfixExpr.
  InfixExpr *this = (InfixExpr *)expr;
  Value *v1 = this->left->eval( this->left, env );
  bool result1 = false;
  bool result2 = false;
  Value *zero = makeInteger(0);
  result1 = v1->equals(v1, zero);
  v1->destroy(v1);
  if (result1 == true) {
    zero->destroy(zero);
    return makeInteger(false);
  }
  Value *v2 = this->right->eval( this->right, env );
  result2 = v2->equals(v2, zero);
  v2->destroy(v2);
  zero->destroy(zero);
  if (result2 == true)
    return makeInteger(false);
  return makeInteger(true);
}

Expr *makeAnd( Expr *left, Expr *right )
{
  // Use the convenience function to build a InfixExpr for the and test.
  return buildInfixExpr( left, right, evalAnd );
}

static Value *evalOr( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a InfixExpr.
  InfixExpr *this = (InfixExpr *)expr;
  Value *v1 = this->left->eval( this->left, env );
  bool result1 = false;
  bool result2 = false;
  Value *zero = makeInteger(0);
  result1 = v1->equals(v1, zero);
  v1->destroy(v1);
  if (result1 == false) {
    zero->destroy(zero);
    return makeInteger(true);
  }
  Value *v2 = this->right->eval( this->right, env );
  result2 = v2->equals(v2, zero);
  v2->destroy(v2);
  zero->destroy(zero);
  if (result2 == false)
    return makeInteger(true);
  return makeInteger(false);
}

Expr *makeOr( Expr *left, Expr *right )
{
  // Use the convenience function to build a InfixExpr for the and test.
  return buildInfixExpr( left, right, evalOr );
}

//////////////////////////////////////////////////////////////////////
// Variable


/** Representation for an expression representing an occurrence of a
    variable, subclass of Expr. */
typedef struct {
  Value *(*eval)( Expr *expr, Environment *env );
  void (*destroy)( Expr *expr );

  /** Name of the variable. */
  char name[ MAX_VAR_NAME + 1 ];
} VariableExpr;

static Value *evalVariable( Expr *expr, Environment *env )
{
  // If this function gets called, expr must really be a VariableExpr
  VariableExpr *this = (VariableExpr *) expr;

  // Get the value of this variable.
  Value *val = lookupVariable( env, this->name );
  if ( !val ) {
    fprintf( stderr, "Undefined variable: %s\n", this->name );
    exit( EXIT_FAILURE );
  }

  return val->copy( val );
}

/** Implementation of destroy for Variable. */
static void destroyVariable( Expr *expr )
{
  free( expr );
}

Expr *makeVariable( char const *name )
{
  // Allocate space for the Variable statement, and fill in its function
  // pointers and a copy of the variable name.
  VariableExpr *this = (VariableExpr *) malloc( sizeof( VariableExpr ) );
  this->eval = evalVariable;
  this->destroy = destroyVariable;
  strcpy( this->name, name );

  return (Expr *) this;
}

//////////////////////////////////////////////////////////////////////
// Print

/** Representation for a print statement, derived from Stmt. */
typedef struct {
  void (*execute)( Stmt *stmt, Environment *env );
  void (*destroy)( Stmt *stmt );

  /** Expression for the thing we're supposted to evaluate and then
      print. */
  Expr *expr;
} PrintStmt;

/** Implementation of execute for a PrintStmt */
static void executePrint( Stmt *stmt, Environment *env )
{
  // If this function gets called, stmt must really be a PrintStmt.
  PrintStmt *this = (PrintStmt *)stmt;

  // Evaluate our argument, print the result, then free it.
  Value *v = this->expr->eval( this->expr, env );
  v->print( v, stdout );
  v->destroy( v );
}

/** Implementation of destroy for a PrintStmt */
static void destroyPrint( Stmt *stmt )
{
  // If this function gets called, stmt must really be a PrintStmt.
  PrintStmt *this = (PrintStmt *)stmt;

  // Free our subexpression then the print statement itself.
  this->expr->destroy( this->expr );
  free( this );
}

Stmt *makePrint( Expr *expr )
{
  // Allocate space for the PrintStmt object
  PrintStmt *this = (PrintStmt *) malloc( sizeof( PrintStmt ) );

  // Remember the pointers to execute and destroy this statement.
  this->execute = executePrint;
  this->destroy = destroyPrint;

  // Remember the expression for the thing we're supposed to print.
  this->expr = expr;

  // Return the PrintStmt object, as an instance of the Stmt interface.
  return (Stmt *) this;
}

//////////////////////////////////////////////////////////////////////
// Compound

/** Representation for a compound statement, derived from Stmt. */
typedef struct {
  void (*execute)( Stmt *stmt, Environment *env );
  void (*destroy)( Stmt *stmt );

  /** Number of statements in the compound. */
  int len;
  
  /** List of statements in the compound. */
  Stmt **stmtList;
} CompoundStmt;

/** Implementation of execute for CompountStmt */
static void executeCompound( Stmt *stmt, Environment *env )
{
  // If this function gets called, stmt must really be a CompoundStmt.
  CompoundStmt *this = (CompoundStmt *)stmt;

  // Execute the sequence of statements in this compound
  for ( int i = 0; i < this->len; i++ )
    this->stmtList[ i ]->execute( this->stmtList[ i ], env );
}

/** Implementation of destroy for CompountStmt */
static void destroyCompound( Stmt *stmt )
{
  // If this function gets called, stmt must really be a CompoundStmt.
  CompoundStmt *this = (CompoundStmt *)stmt;

  // Free the list of statements inside this compond.
  for ( int i = 0; i < this->len; i++ )
    this->stmtList[ i ]->destroy( this->stmtList[ i ] );

  // Then, free the list of pointers and the compund statement itself.
  free( this->stmtList );
  free( this );
}

Stmt *makeCompound( int len, Stmt **stmtList )
{
  // Allocate space for the CompoundStmt object
  CompoundStmt *this = (CompoundStmt *) malloc( sizeof( CompoundStmt ) );

  // Remember the pointers to execute and destroy this statement.
  this->execute = executeCompound;
  this->destroy = destroyCompound;

  // Remember the list of statements in the compound.
  this->len = len;
  this->stmtList = stmtList;

  // Return the result, as an instance of the Stmt interface.
  return (Stmt *) this;
}

///////////////////////////////////////////////////////////////////////

// While/If

/** Representation for either a while or if statement, subclass of Stmt. */
typedef struct {
  void (*execute)( Stmt *stmt, Environment *env );
  void (*destroy)( Stmt *stmt );

  // Condition to be checked before running the body.
  Expr *cond;

  // Body to execute if / while cond is true.
  Stmt *body;
} ConditionalStmt;

/** Implementation of destroy for either while of if statements. */
static void destroyConditional( Stmt *stmt )
{
  // If this function gets called, stmt must really be a ConditionalStmt.
  ConditionalStmt *this = (ConditionalStmt *)stmt;

  // Destroy the condition expression and the statement in the body.
  this->cond->destroy( this->cond );
  this->body->destroy( this->body );

  // Then, free the ConditionalStmt struct.
  free( this );
}

/** Implementation of th execute function for an if statement. */
static void executeIf( Stmt *stmt, Environment *env )
{
  // If this function gets called, stmt must really be a ConditionalStmt.
  ConditionalStmt *this = (ConditionalStmt *)stmt;

  // It would be better to make this once and re-use it.
  Value *falseValue = makeInteger( 0 );

  // Evaluate our operand and see if it's true.
  Value *result = this->cond->eval( this->cond, env );
  bool flag = ! ( result->equals( result, falseValue ) );
  result->destroy( result );

  // Execute the body if the condition evaluated to true.
  if ( flag )
    this->body->execute( this->body, env );

  falseValue->destroy( falseValue );
}

Stmt *makeIf( Expr *cond, Stmt *body )
{
  ConditionalStmt *this =
    (ConditionalStmt *) malloc( sizeof( ConditionalStmt ) );

  this->execute = executeIf;
  this->destroy = destroyConditional;

  this->cond = cond;
  this->body = body;

  // Return the result, as an instance of the Stmt interface.
  return (Stmt *) this;
}

/** Implementation of th execute function for a while statement. */
static void executeWhile( Stmt *stmt, Environment *env )
{
  // If this function gets called, stmt must really be a ConditionalStmt.
  ConditionalStmt *this = (ConditionalStmt *)stmt;

  // It would be better to make this once and re-use it.
  Value *falseValue = makeInteger( 0 );

  // Evaluate our condition and see if it's true.
  Value *result = this->cond->eval( this->cond, env );
  bool flag = ! ( result->equals( result, falseValue ) );
  result->destroy( result );

  // Execute the body while the condition evaluates to true.
  while ( flag ) {
    this->body->execute( this->body, env );
    
    // Get the value of the condition for the next iteration.
    result = this->cond->eval( this->cond, env );
    flag = ! ( result->equals( result, falseValue ) );
    result->destroy( result );
  }

  falseValue->destroy( falseValue );


  // Execute the condition as long as the body evaluated to true.
  while ( toInt( this->cond->eval( this->cond, env ) ) )
    this->body->execute( this->body, env );
}

Stmt *makeWhile( Expr *cond, Stmt *body )
{
  ConditionalStmt *this =
    (ConditionalStmt *) malloc( sizeof( ConditionalStmt ) );

  this->execute = executeWhile;
  this->destroy = destroyConditional;

  this->cond = cond;
  this->body = body;

  // Return the result, as an instance of the Stmt interface.
  return (Stmt *) this;
}


// Assignment

/** Representation of an assignment statement, a subclass of Stmt. */
typedef struct {
  void (*execute)( Stmt *stmt, Environment *env );
  void (*destroy)( Stmt *stmt );

  char name[ MAX_VAR_NAME + 1 ];
  Expr *expr;
} AssignmentStmt;

/** Implementation of destroy for assignment Statements. */
static void destroyAssignment( Stmt *stmt )
{
  AssignmentStmt *this = (AssignmentStmt *)stmt;

  this->expr->destroy( this->expr );
  free( this );
}

/** Implementation of execute for assignment Statements. */
static void executeAssignment( Stmt *stmt, Environment *env )
{
  AssignmentStmt *this = (AssignmentStmt *) stmt;

  Value *result = this->expr->eval( this->expr, env );
  setVariable( env, this->name, result );
}

Stmt *makeAssignment( char const *name, Expr *expr )
{
  AssignmentStmt *this =
    (AssignmentStmt *) malloc( sizeof( AssignmentStmt ) );

  this->execute = executeAssignment;
  this->destroy = destroyAssignment;

  strcpy( this->name, name );
  this->expr = expr;

  // Return the result, as an instance of the Stmt interface.
  return (Stmt *) this;
}

