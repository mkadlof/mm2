JFLAGS=#-g
JC=javac -classpath .

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  VecNd.java \
		  Atom.java \
		  TheSystem.java \
		  Integrator.java \
		  Verlet.java \
		  Leapfrog.java \
			VelocityVerlet.java \
			VelocityVerlet3.java \
			Beeman.java \
			Langevin.java \
			Andersen.java \
		  ForceField.java \
		  ForceFieldDecorator.java \
		  NoField.java \
		  VanDerWaals.java \
		  SoftWalls.java \
			TwoWells.java \
			TwoWells2.java \
		  Input.java \
			Output.java \
		  TrajectoryAndStats.java \
		  Simulation.java 



default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
