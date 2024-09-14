#!/bin/bash
FAIL=0

# Function to run the program against a test case.
runtest() {
  TESTNO=$1
  ESTATUS=$2

  rm -f output.txt stderr.txt

  echo "Test $TESTNO: ./interpret prog-$TESTNO.txt > output.txt 2> stderr.txt"
  ./interpret prog-$TESTNO.txt > output.txt 2> stderr.txt
  STATUS=$?

  # Make sure the program exited with the right exit status.
  if [ $STATUS -ne $ESTATUS ]; then
      echo "**** Test failed - incorrect exit status. Expected: $ESTATUS Got: $STATUS"
      FAIL=1
      return 1
  fi

  # Make sure the output matches the expected output.
  if ! diff -q expected-$TESTNO.txt output.txt >/dev/null 2>&1
  then
      echo "**** Test FAILED - output doesn't match expected"
      FAIL=1
      return 1
  fi

  # If there is an error message, make sure it matches.
  if [ -s error-$TESTNO.txt ]; then
      
      if ! diff -q error-$TESTNO.txt stderr.txt >/dev/null 2>&1
      then
	 echo "**** Test FAILED - error output doesn't match expected"
	 FAIL=1
	 return 1
      fi
  else
      
      if [ -s stderr.txt ]; then
	 echo "**** Test FAILED - output printed to stderr (but shouldn't be for this test)."
	 FAIL=1
	 return 1
      fi
  fi

  echo "Test $TESTNO PASS"
  return 0
}

# make a fresh copy of the target program
rm -f interpret
make clean
make

# Run all the tests if the program compiled successfully.
if [ -x interpret ]; then
    # Run each of the test cases.
    runtest 01 0
    runtest 02 0
    runtest 03 0
    runtest 04 0
    runtest 05 0
    runtest 06 0
    runtest 07 0
    runtest 08 0
    runtest 09 0
    runtest 10 0
    runtest 11 0
    runtest 12 0
    runtest 13 0
    runtest 14 0
    runtest 15 0
    runtest 16 0
    runtest 17 0
    runtest 18 0
    runtest 19 0
    runtest 20 1

else
    echo "**** The program didn't compile successfully"
    FAIL=1
fi


if [ $FAIL -ne 0 ]; then
  echo "FAILING TESTS!"
  exit 13
else 
  exit 0
fi
