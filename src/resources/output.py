# run the java program to generate the output
# java source code is in ../src/java
import os
import subprocess

OUTPUT_DIRECTORY = "outputs"
INPUT_DIRECTORY = "inputs"
OUTPUT_EXTENSION = "out"
JAR_PATH = "../../out/artifacts/CMPE250_p3_jar/CMPE250_p3.jar"

if __name__ == "__main__":
    for inn in sorted(os.listdir(INPUT_DIRECTORY)):
        print("generating output for ", inn)
        if inn.endswith(".in"):
            case_name: str = inn[:-3]
        else:
            print("skipping ", input)
            continue


        input_path: str = f"{INPUT_DIRECTORY}/{inn}"
        output_path: str = f"{OUTPUT_DIRECTORY}/{case_name}.{OUTPUT_EXTENSION}"

        subprocess.run(["java", "-jar", JAR_PATH, input_path, output_path])