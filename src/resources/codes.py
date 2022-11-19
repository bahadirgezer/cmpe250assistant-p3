# create all possible airport code permutations (Three capital letters) and write them to a JSON file
import json
import string


def generate_airport_codes():
    airport_codes = []
    for a in string.ascii_uppercase:
        for b in string.ascii_uppercase:
            for c in string.ascii_uppercase:
                airport_codes.append(f"{a}{b}{c}")
    # print(len(airport_codes)) -> 17576
    with open("data/airport_codes.json", "w") as file:
        json.dump(airport_codes, file)


def generate_acc_codes():
    acc_codes = []
    for a in string.ascii_uppercase:
        for b in string.ascii_uppercase:
            for c in string.ascii_uppercase:
                for d in string.ascii_uppercase:
                    acc_codes.append(f"{a}{b}{c}{d}")
    # print(len(atc_codes)) -> 456976
    with open("data/acc_codes.json", "w") as file:
        json.dump(acc_codes, file)

# AA1111 - ZZ9999
def generate_flight_codes():
    flight_codes = []
    for a in string.ascii_uppercase:
        for b in string.ascii_uppercase:
            for c in range(1, 10):
                for d in range(0, 10):
                    for e in range(0, 10):
                        for f in range(0, 10):
                            flight_codes.append(f"{a}{b}{c}{d}{e}{f}")
    # print(len(flight_codes)) -> 17576000
    with open("data/flight_codes.json", "w") as file:
        json.dump(flight_codes, file)

def main():
    generate_airport_codes()
    generate_acc_codes()
    generate_flight_codes()

if __name__ == "__main__":
    main()
