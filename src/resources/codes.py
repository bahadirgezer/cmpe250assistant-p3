# create all possible airport code permutations (Three capital letters) and write them to a JSON file
import json
import string


def generate_airport_codes():
    airport_codes = []
    for a in string.ascii_uppercase:
        for b in string.ascii_uppercase:
            for c in string.ascii_uppercase:
                airport_codes.append(f"{a}{b}{c}")

    with open("data/airport_codes.json", "w") as file:
        json.dump(airport_codes, file)


def generate_atc_codes():
    atc_codes = []
    for a in string.ascii_uppercase:
        for b in string.ascii_uppercase:
            for c in string.ascii_uppercase:
                for d in string.ascii_uppercase:
                    atc_codes.append(f"{a}{b}{c}{d}")

    with open("data/atc_codes.json", "w") as file:
        json.dump(atc_codes, file)


def main():
    # generate_airport_codes()
    generate_atc_codes()


if __name__ == "__main__":
    main()
