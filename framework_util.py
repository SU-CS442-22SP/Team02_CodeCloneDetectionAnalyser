import os
import argparse
import shutil


def CC_output_generator(operation_type):
    file_content = {}
    input_file = ''
    output_directory = ''
    suffix = ''
    title = ''

    if operation_type == 'simpleCC':
        input_file = 'Type2_SimpleCC_All_Pairs.txt'
        output_directory = 'Type2_SimpleCC_Results'
        suffix = '_simpleCC_results.txt'
        title = 'SimpleCC Results for the files: '
    elif operation_type == 'myCC':
        input_file = 'Type2_myCC_All_Pairs.txt'
        output_directory = 'Type2_myCC_Results'
        suffix = '_myCC_results.txt'
        title = 'myCC Results for the files: '
    else:
        print("Invalid operation type. Choose either 'simpleCC' or 'myCC'.")
        return

    with open(input_file, 'r') as file:
        data = file.read().split('<pair>')

        for pair in data:
            pair = pair.strip().replace('</pair>', '')
            lines = pair.split('\n')

            if len(lines) < 2 or (operation_type == 'myCC' and ("Comparing file" in lines[0] or "Comparing file" in lines[1])):
                continue

            file1_name = lines[0].split(',')[0].split('\\')[-1]
            file1_content = '<pair>\n' + lines[0]

            file2_name = lines[1].split(',')[0].split('\\')[-1]
            file2_content = lines[1] + '\n</pair>'

            if file2_name == file1_name:
                continue

            file_name = '-'.join(sorted([file1_name, file2_name])) + suffix

            if file_name in file_content:
                file_content[file_name].append(file1_content)
                file_content[file_name].append(file2_content)
            else:
                file_content[file_name] = [file1_content, file2_content]

    if not os.path.exists(output_directory):
        os.makedirs(output_directory)

    for file_name, contents in file_content.items():
        with open(os.path.join(output_directory, file_name), 'w') as f:
            f.write(title + file_name.replace(suffix,
                    '').replace('-', ' - ') + '\n\n')
            for content in contents:
                f.write(content + '\n')


def overall_cc_output_generator():
    # Directories to merge files from
    dir1 = 'Type1_CC_Results'
    dir2 = 'Type2_SimpleCC_Results'
    dir3 = 'Type2_myCC_Results'

    # Check if directories exist
    if not os.path.exists(dir1) or not os.path.exists(dir2) or not os.path.exists(dir3):
        print(f"Directories '{dir1}', '{dir2}' and '{dir3}' must exist.")
        return

    # Create output directory if it doesn't exist
    out_dir = 'Total_CC_Result'
    if not os.path.exists(out_dir):
        os.makedirs(out_dir)

    # Transform filenames to a common format for matching
    dir1_files = [filename.replace('_results.txt', '')
                  for filename in os.listdir(dir1)]
    dir2_files = [filename.replace('_simpleCC_results.txt', '')
                  for filename in os.listdir(dir2)]
    dir3_files = [filename.replace('_myCC_results.txt', '')
                  for filename in os.listdir(dir3)]

    # Concatenate all the filenames
    all_files = set(dir1_files + dir2_files + dir3_files)

    for file in all_files:
        output_filename = os.path.join(out_dir, file + '_total_cc_result.txt')

        if file in dir1_files:
            with open(os.path.join(dir1, file + '_results.txt'), 'r') as infile:
                content = infile.read()
            with open(output_filename, 'w') as outfile:  # 'w' stands for 'write'
                outfile.write(content)

        if file in dir2_files:
            with open(os.path.join(dir2, file + '_simpleCC_results.txt'), 'r') as infile:
                content = infile.read()
            with open(output_filename, 'a') as outfile:  # 'a' stands for 'append'
                outfile.write('\n' + '-'*200 + '\n')
                outfile.write(content)

        if file in dir3_files:
            with open(os.path.join(dir3, file + '_myCC_results.txt'), 'r') as infile:
                content = infile.read()
            with open(output_filename, 'a') as outfile:  # 'a' stands for 'append'
                outfile.write('\n' + '-'*200 + '\n')
                outfile.write(content)


def main(arg):
    if arg == "simpleCC" or arg == "myCC":
        CC_output_generator(arg)
    elif arg == "all":
        overall_cc_output_generator()
    else:
        print("Invalid argument value")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("arg", type=str, help="The argument value")
    args = parser.parse_args()
    main(args.arg)
