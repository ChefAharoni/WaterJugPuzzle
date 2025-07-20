#!/bin/bash

file=WaterJugPuzzleSolver.java
MAXTIME="1.0"

if [ ! -f "$file" ]; then
    echo -e "Error: File '$file' not found.\nTest failed."
    exit 1
fi

num_right=0
total=0
line="________________________________________________________________________"
compiler=
interpreter=
language=
extension=${file##*.}
if [ "$extension" = "py" ]; then
    if [ ! -z "$PYTHON_PATH" ]; then
        interpreter=$(which python.exe)
    else
        interpreter=$(which python3.2)
    fi
    command="$interpreter $file"
    echo -e "Testing $file\n"
elif [ "$extension" = "java" ]; then
    language="java"
    command="java ${file%.java}"
    echo -n "Compiling $file..."
    javac $file
    echo -e "done\n"
elif [ "$extension" = "c" ] || [ "$extension" = "cpp" ]; then
    language="c"
    command="./${file%.*}"
    echo -n "Compiling $file..."
    results=$(make 2>&1)
    if [ $? -ne 0 ]; then
        echo -e "\n$results"
        exit 1
    fi
    echo -e "done\n"
fi

run_test_args() {
    (( ++total ))
    echo -n "Running test $total..."
    expected=$2
    expected_return_val=$3
    local ismac=0
    date --version >/dev/null 2>&1
    if [ $? -ne 0 ]; then
       ismac=1
    fi
    local start=0
    if (( ismac )); then
        start=$(python -c 'import time; print(time.time())')
    else
        start=$(date +%s.%N)
    fi
    $command $1 2>&1 | tr -d '\r' > tmp.txt
    retval=${PIPESTATUS[0]}
    local end
    if (( ismac )); then
        end=$(python -c 'import time; print(time.time())')
    else
        end=$(date +%s.%N)
    fi
    received=$(cat tmp.txt)
    local elapsed=$(echo "scale=3; $end - $start" | bc | awk '{printf "%.3f", $0}')
    if (( $(echo "$elapsed > $MAXTIME" | bc -l) )); then
        echo -e "failure [timeout after $MAXTIME seconds]\n"
    elif [ "$expected" != "$received" ]; then
        echo -e "failure\n\nExpected$line\n$expected\n"
        echo -e "Received$line\n$received\n"
    else
        if [ "$expected_return_val" = "$retval" ]; then
            echo "success [$elapsed seconds]"
            (( ++num_right ))
        else
            echo "failure Return value is $retval, expected $expected_return_val."
        fi
    fi
    rm -f tmp.txt input.txt
}

run_test_args "" "Usage: java WaterJugPuzzleSolver <cap A> <cap B> <cap C> <goal A> <goal B> <goal C>" "1"
run_test_args "1 2 3 4 5 6 7" "Usage: java WaterJugPuzzleSolver <cap A> <cap B> <cap C> <goal A> <goal B> <goal C>" "1"
run_test_args "x 2 3 4 5 6" "Error: Invalid capacity 'x' for jug A." "1"
run_test_args "1 -2 3 4 5 6" "Error: Invalid capacity '-2' for jug B." "1"
run_test_args "1 2 0 4 5 6" "Error: Invalid capacity '0' for jug C." "1"
run_test_args "1 2 3 y 5 6" "Error: Invalid goal 'y' for jug A." "1"
run_test_args "1 2 3 4 -5 6" "Error: Invalid goal '-5' for jug B." "1"
run_test_args "1 2 3 4 5 -1" "Error: Invalid goal '-1' for jug C." "1"
run_test_args "3 5 8 4 0 4" "Error: Goal cannot exceed capacity of jug A." "1"
run_test_args "3 5 8 0 6 2" "Error: Goal cannot exceed capacity of jug B." "1"
run_test_args "3 5 8 0 0 9" "Error: Goal cannot exceed capacity of jug C." "1"
run_test_args "3 5 8 2 1 4" "Error: Total gallons in goal state must be equal to the capacity of jug C." "1"
run_test_args "6 7 10 5 5 0" "No solution." "0"
run_test_args "6 7 10 0 0 10" "Initial state. (0, 0, 10)" "0"
run_test_args "3 5 8 0 5 3" "Initial state. (0, 0, 8)"$'\n'"Pour 5 gallons from C to B. (0, 5, 3)" "0"

echo -e "\nTotal tests run    : $total"
echo -e "Number correct     : $num_right"
echo -n "Percent correct: "
echo "scale=2; 100 * $num_right / $total" | bc

if [ "$language" = "java" ]; then
    echo -e -n "\nRemoving class files..."
    rm -f *.class
    echo "done"
elif [ "$language" = "c" ]; then
    echo -e -n "\nCleaning project..."
    make clean > /dev/null 2>&1
    echo "done"
fi
