import os
import difflib


def compare_code_lines(code1, code2):
    """Compare two pieces of code and return similarity ratio."""
    s = difflib.SequenceMatcher(None, code1, code2)
    return s.ratio()


def get_java_files(directory):
    """Traverse the directory and return all Java files."""
    java_files = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".java"):
                java_files.append(os.path.join(root, file))
    return java_files


def read_java_file(filename):
    """Read a Java file and return its lines of code."""
    with open(filename, "r") as source_code:
        lines = source_code.readlines()
        return lines


def get_details_dir(directory_path):
    directory = directory_path
    java_files = get_java_files(directory)
    min_line_length = 10  # minimum number of characters for a line to be considered

    # Compare all files with each other
    for i in range(len(java_files)):
        for j in range(i+1, len(java_files)):
            file1 = java_files[i]
            file2 = java_files[j]

            lines1 = read_java_file(file1)
            lines2 = read_java_file(file2)

            for index1, line1 in enumerate(lines1):
                for index2, line2 in enumerate(lines2):
                    if len(line1.strip()) < min_line_length:
                        continue
                    similarity = compare_code_lines(
                        line1.strip(), line2.strip())

                    if similarity == 1:  # adjust this threshold as needed
                        print(
                            f"<pair>\n{file1},{index1+1},{line1.strip()}\n{file2},{index2+1},{line2.strip()}\n<pair>")





def get_details_2files(f1, f2, total_result):
    min_line_length = 10  # minimum number of characters for a line to be considered

    lines1 = read_java_file(f1)
    lines2 = read_java_file(f2)

    # Get the base names of the files
    base_name_f1 = os.path.basename(f1)
    base_name_f2 = os.path.basename(f2)

    # Prepare the directory and file name
    results_dir = "./Type1_CC_Results"
    if not os.path.exists(results_dir):
        os.makedirs(results_dir)
    result_file_name = f"{results_dir}/{base_name_f1}-{base_name_f2}_results.txt"

    # Open the file in write mode to clear any previous contents
    with open(result_file_name, 'w') as result_file:
        result_file.write(f"COMPARISON FOR THE FILES: {base_name_f1} - {base_name_f2}\n")
        result_file.write(f"TOTAL TYPE 1 CODE CLONE PERCENTAGE: {total_result}\n\n")

    for index1, line1 in enumerate(lines1):
        for index2, line2 in enumerate(lines2):
            if len(line1.strip()) < min_line_length:
                continue
            similarity = compare_code_lines(line1.strip(), line2.strip())

            if similarity == 1:  
                # Open the file in append mode and write the results
                with open(result_file_name, 'a') as result_file:
                    result_file.write(
                        f"<pair>\n{f1},Line:{index1+1},{line1.strip()}\n{f2},Line:{index2+1},{line2.strip()}\n<pair>\n")
