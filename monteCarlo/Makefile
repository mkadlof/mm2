JFLAGS=#-g
JC=javac -classpath .

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
			Vec.java \
			VecDouble.java \
			TrajectoryAndStats.java \
			Constraints.java \
			Configuration.java \
			Chain.java \
			Thermostat.java \
			SimulatedAnnealing.java \
			ReplicaExchange.java


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
