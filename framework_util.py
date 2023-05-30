import os
import argparse


def simpleCC_output_generator():
    file_content = {}
    with open('Type2_SimpleCC_All_Pairs.txt', 'r') as file:
        data = file.read().split('</pair>')

        for pair in data:
            pair = pair.strip().replace('<pair>\n', '')
            lines = pair.split('\n')

            if len(lines) < 2:
                continue

            # Split file name from content
            file1_name = lines[0].split(',')[0].split('\\')[-1]
            file1_content = '<pair>\n' + lines[0]

            file2_name = lines[1].split(',')[0].split('\\')[-1]
            file2_content = lines[1] + '\n</pair>'

            if file2_name == file1_name:
                continue

            # Generate file name as 'file_num1-file_num2_simpleCC_results.txt' ensuring consistent order
            file_name = '-'.join(sorted([file1_name, file2_name])
                                 ) + '_simpleCC_results.txt'

            # If file already exists in the dictionary, append the new content. Otherwise, create a new entry
            if file_name in file_content:
                file_content[file_name].append(file1_content)
                file_content[file_name].append(file2_content)
            else:
                file_content[file_name] = [file1_content, file2_content]

    # Create new directory if not exists
    if not os.path.exists('Type2_SimpleCC_Results'):
        os.makedirs('Type2_SimpleCC_Results')

    # Write the data to respective files
    for file_name, contents in file_content.items():
        with open(os.path.join('Type2_SimpleCC_Results', file_name), 'w') as f:
            # Write the file title as the first line, remove "_simpleCC_results" from the header
            f.write('SimpleCC Results for the files: ' +
                    file_name.replace('_simpleCC_results.txt', '').replace('-', ' - ') + '\n\n')
            for content in contents:
                f.write(content + '\n')


def overall_cc_output_generator():
    # Directories to merge files from
    dir1 = 'Type1_CC_Results'
    dir2 = 'Type2_SimpleCC_Results'

    # Check if directories exist
    if not os.path.exists(dir1) or not os.path.exists(dir2):
        print(f"Both directories '{dir1}' and '{dir2}' must exist.")
        return

    # Get file names from both directories
    dir1_files = os.listdir(dir1)
    dir2_files = os.listdir(dir2)

    # Transform file names for matching
    dir1_files_transformed = {
        f.replace('_results.txt', ''): f for f in dir1_files}
    dir2_files_transformed = {
        f.replace('_simpleCC_results.txt', ''): f for f in dir2_files}

    # Create output directory if it doesn't exist
    out_dir = 'Total_CC_Result'
    if not os.path.exists(out_dir):
        os.makedirs(out_dir)

    # For each file in dir2, check if it exists in dir1 and merge them
    for file_transformed, file_original in dir2_files_transformed.items():
        if file_transformed in dir1_files_transformed:
            # Open the output file in write mode
            with open(os.path.join(out_dir, file_transformed + '_total_cc_result.txt'), 'w') as outfile:
                # Write contents of file from dir1
                with open(os.path.join(dir1, dir1_files_transformed[file_transformed]), 'r') as infile:
                    outfile.write(infile.read())

                outfile.write('\n' + '-'*200 + '\n')

                # Write contents of file from dir2
                with open(os.path.join(dir2, file_original), 'r') as infile:
                    outfile.write(infile.read())


def main(arg):
    if (arg == 1):
        simpleCC_output_generator()
    elif arg == 2:
        overall_cc_output_generator()
    else:
        print("Invalid argument value")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("arg", type=int, help="The argument value")
    args = parser.parse_args()
    main(args.arg)
