JFLAGS=#-g
JC=javac -classpath .

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		 	TheSystem.java \
		 	Integrator.java \
		  	ForceField.java \
		  	TrajectoryAndStats.java \
			Output.java \
		  	Simulation.java 



default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
