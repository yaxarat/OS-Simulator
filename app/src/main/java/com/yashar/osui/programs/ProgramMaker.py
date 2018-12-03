from random import randint

number = input("Enter amount of programs you want to create: ")
programs = []
i = 0
n = 0


while i < int(number):
    path = "program{}.txt".format(i)
    name = "program{}".format(i)
    command_len = randint(10, 40)
    programs.append(name)
    file = open(path, "w+")
    file.write("{}\n".format(str(randint(50, 800))))

    while command_len > 0:
        option = randint(0, 3)

        if option == 0:
            file.write("CALCULATE {}\n".format(str(randint(100, 500))))
        elif option == 1:
            file.write("IO\n")
        elif option == 2:
            file.write("YIELD\n")
        else:
            file.write("OUT\n")

        command_len -= 1

    file.close()
    i += 1

with open('job.txt', 'w') as file_handle:
    for list_item in programs:
        file_handle.write("LOAD {}\n".format(list_item))
    file_handle.write("EXE")
