import os
import argparse

def simpleCC_Merger():
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

            # Generate file name as 'file_num1-file_num2.txt' ensuring consistent order
            file_name = '-'.join(sorted([file1_name, file2_name])) + 'Type2.txt'

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
            for content in contents:
                f.write(content + '\n')

def main(arg):
    if(arg == 1):
        simpleCC_Merger()
    else:
        print("Invalid argument value")

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("arg", type=int, help="The argument value")
    args = parser.parse_args()
    main(args.arg)