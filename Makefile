.PHONY: all clean

vpath %.scala ./

all: build/Basic.sv build/Hierarchy.sv build/Function.sv

clean:
	rm -rf build/

build/%.sv: %.scala
	scala-cli $<
