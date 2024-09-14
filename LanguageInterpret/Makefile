# Standard command-line options
CFLAGS = -g -Wall -std=c99

# Build the executable.
interpret: interpret.o stringval.o parse.o syntax.o value.o

# Build each of our object files when one of their dependencies changes.
interpret.o: interpret.c stringval.h parse.h syntax.h value.h

parse.o: parse.c stringval.h parse.h syntax.h value.h

stringval.o: stringval.c stringval.h syntax.h value.h

syntax.o: syntax.c syntax.h value.h

value.o: value.c value.h

# Remove all targets for a fresh rebuild.
clean:
	rm -f interpret.o parse.o stringval.o syntax.o value.o
	rm -f interpret
