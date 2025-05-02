import subprocess
import os
import shutil

def check_maven(maven_path=None):
    """checking if Maven is available."""
    if maven_path:
        return os.path.exists(maven_path)
    return shutil.which("mvn") is not None

def compile_project(maven_path=None):
    """it will compile the Maven project."""
    # Default Maven path for my installation
    default_maven_path = r"C:\Users\Sai prasanna\Downloads\apache-maven-3.9.9-bin\apache-maven-3.9.9\bin\mvn.cmd"
    maven_path = default_maven_path
    
    if not check_maven(maven_path):
        print(f"Error: Maven not found in {maven_path}.")
        exit(1)
    
    mvn_cmd = [maven_path, "clean", "package"]
    try:
        result = subprocess.run(mvn_cmd, capture_output=True, text=True)
        if result.returncode != 0:
            print("Compilation failed:")
            print(result.stderr)
            exit(1)
        print("Compilation successful.")
    except FileNotFoundError:
        print(f"Error: Maven executable not found at {mvn_cmd[0]}.")
        exit(1)

def run_test(test_case):
    """Run a single test case using the generated JAR."""
    jar_path = "aarithmetic.jar"
    if not os.path.exists(jar_path):
        print(f"JAR file {jar_path} not found.")
        exit(1)
    cmd = ["java", "-jar", jar_path] + test_case
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        print(f"Test failed: {' '.join(test_case)}")
        print(result.stderr)
        return False
    print(f"{' '.join(test_case)}")
    print(f"Output: {result.stdout.strip()}")
    return True

def main():
    compile_project()
    test_cases = [
        ["int", "add", "23650078224912949497310933240250", "42939783262467113798386384401498"],
        ["int", "sub", "3116511674006599806495512758577", "57745242300346381144446453884008"],
        ["int", "mul", "14344163160445929942680697312322", "23017167694823904478474013730519"],
        ["int", "div", "8792726365283060579833950521677211", "493835253617089647454998358"],
        ["float", "div", "8792726365283060579833950521677211.0", "493835253617089647454998358"],
        ["float", "add", "84486723.420039", "70974199.843732"],
        ["float", "sub", "840196454.51725", "712586963.70283"],
        ["float", "mul", "6400251.9377695", "2326541.6827934"],
        ["float", "div", "244727.15202", "75964.3891"],
        ["int", "div", "25", "123"],
        ["float", "div", "3227", "555"],
        ["float", "div", "5.5", "2"],
        ["int", "div", "2", "0"],
    ]
    for test in test_cases:
        run_test(test)
if __name__ == "__main__":
    main()