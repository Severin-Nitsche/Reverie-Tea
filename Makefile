project:
	find src -name "*.java" | xargs javac --enable-preview --release 15 -d out --module-source-path src

annotated:
	echo 'Error: not supported yet'

jar:
	jar -cf Library.jar out
