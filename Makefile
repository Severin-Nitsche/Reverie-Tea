project:
	find src -name "*.java" | xargs javac --enable-preview --release 15 -d out/production --module-source-path src

annotated:
	echo 'Error: not supported yet'

jar: project
	mkdir -p out/artifacts
	jar -cf out/artifacts/AlgebraicDataTypes.jar -C out/production/com.github.severinnitsche.AlgebraicDataTypes .
	jar -cf out/artifacts/Dreamer.jar -C out/production/com.github.severinnitsche.Dreamer .
	jar -cf out/artifacts/fantasyland.jar -C out/production/com.github.severinnitsche.fantasyland .
	jar -cf out/artifacts/Function.jar -C out/production/com.github.severinnitsche.Function .
	jar -cf out/artifacts/FunctionGenerator.jar -C out/production/com.github.severinnitsche.FunctionGenerator .
	jar -cf out/artifacts/MilnerTypes.jar -C out/production/com.github.severinnitsche.MilnerTypes .
	jar -cf out/artifacts/ReverieConceiver.jar -C out/production/com.github.severinnitsche.ReverieConceiver .

test: project
	java --enable-preview --module-path out/production --module com.github.severinnitsche.MilnerTypes/com.github.severinnitsche.milner_types.Example
