# run the java program to generate the output
# java source code is in ../src/java
import os
from subprocess import Popen, PIPE, STDOUT

OUTPUT_DIRECTORY = "outputs"
INPUT_DIRECTORY = "inputs"
OUTPUT_EXTENSION = "out"
JAR_PATH = "../../out/artifacts/CMPE250_p3_jar/CMPE250_p3.jar"

if __name__ == "__main__":
    cases = ["18"]  # ["14", "15", "16"]
    for inn in sorted(os.listdir(INPUT_DIRECTORY)):
        if inn.endswith(".in"):
            case_name: str = inn[:-3].strip()
            # if list has at least one true value, then the case is in the list
            if [case_name.endswith(c) for c in cases].count(True) == 0:
                # print("skipping case", case_name)
                continue
        else:
            # print("skipping ", input)
            continue

        print("generating output for ", inn)
        input_path: str = f"{INPUT_DIRECTORY}/{inn}"
        output_path: str = f"{OUTPUT_DIRECTORY}/{case_name}.{OUTPUT_EXTENSION}"

        p = Popen(["java", "-jar", JAR_PATH, input_path, output_path], stdin=PIPE,
                  stdout=PIPE, stderr=STDOUT)
        for line in p.stdout:
            print(line.decode("utf-8").strip())
        print("")
        # subprocess.run(["java", "-jar", JAR_PATH, input_path, output_path], stdout=subprocess.PIPE, shell=True)
