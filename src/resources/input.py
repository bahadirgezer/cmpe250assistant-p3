import json
import random
import threading
from time import time

DESTINATION_DIRECTORY = "inputs"
INPUT_NAME = "case"
INPUT_EXTENSION = "in"
AIRPORTS_PER_ACC = 10


class ACC:
    def __init__(self, code: str) -> None:
        self.code: str = code
        self.atcs: list[ATC] = list()
        self.airport_table = [None] * 1000

    def can_hash(self, airport_code: str) -> bool:
        return self.hash(airport_code) != -1

    def hash(self, airport_code: str) -> int:
        str_val: int = (ord(airport_code[0]) + ord(airport_code[1]) * 31 + ord(airport_code[2]) * 31 * 31) % 1000
        for _ in range(1000):
            if self.airport_table[str_val] is None:
                # noinspection PyTypeChecker
                self.airport_table[str_val] = airport_code
                self.atcs.append(ATC(airport_code, str_val, self.code))
                return str_val
            str_val = (str_val + 1) % 1000
        return -1

    # 2. Next <number of ACCs> lines: <ACC code> <airport code> <airport code> ... <airport code>
    def __repr__(self) -> str:
        return f"{self.code} {' '.join([atc.airport for atc in self.atcs])}"


class ATC:
    def __init__(self, airport_code: str, id_val: int, acc_code: str) -> None:
        # id_val should have leading zeros
        self.code: str = acc_code + str(id_val).zfill(3)
        self.airport: str = airport_code

    def __repr__(self) -> str:
        return "".join(self.code)


class Flight:
    def __init__(self, admission_time: int, flight_code: str, acc_code: str,
                 departure_airport_code: str, arrival_airport_code: str, op_times: list[int]) -> None:
        self.admission_time: int = admission_time
        self.flight_code: str = flight_code
        self.acc_code: str = acc_code
        self.departure_airport_code: str = departure_airport_code
        self.arrival_airport_code: str = arrival_airport_code
        self.op_times: list[int] = op_times

    # 3. Next <number of flights> lines: <admission time> <flight code> <ACC code> <departure airport code> <arrival
    # airport code> <list of operation times>
    def __repr__(self) -> str:
        return f"{self.admission_time} {self.flight_code} {self.acc_code} {self.departure_airport_code} {self.arrival_airport_code} {' '.join([str(opTime) for opTime in self.op_times])}"


class Input:
    def __init__(self, num_accs: int, num_flights: int, mode: str = "single") -> None:
        self.flight_codes: list[str] = random.sample(list(json.loads(
            open("data/flight_codes.json", "r").read())), num_flights)
        self.num_accs = num_accs
        self.num_flights = num_flights
        self.mode = mode
        self.flights: list[Flight] = []
        self.accs: list[ACC] = []
        self.generate()

    def generate(self) -> None:
        # create accs
        self.accs: list[ACC] = self.choose_accs()
        airport_codes = set(json.loads(open("data/airport_codes.json", "r").read()))
        print("Creating airports...")
        for acc in self.accs:
            # choose airport codes until at least two airports are hashed
            while len(acc.atcs) < 2:
                airport_code: str = random.choice(list(airport_codes))
                if acc.can_hash(airport_code):
                    airport_codes.remove(airport_code)

            for _ in range(random.randint(0, AIRPORTS_PER_ACC)):  # 2 - 202
                # airports/atcs per acc
                airport_code: str = random.choice(list(airport_codes))
                if acc.can_hash(airport_code):
                    airport_codes.remove(airport_code)

        # create flights
        lock = threading.Lock()
        threads = []
        # each thread will process 100 flights
        print("Creating flights...")
        for _ in range(self.num_flights // 10000):
            print(_, end=" ")
            thread = threading.Thread(target=self.create_flights, args=(10000, lock))
            thread.start()
            threads.append(thread)
        print("\nWaiting for threads to finish...")
        joined_threads = 0
        for thread in threads:
            thread.join()
            joined_threads += 1
            print(joined_threads, end=" ")
        return

    def create_flights(self, num_flights: int, lock: threading.Lock) -> None:
        for _ in range(num_flights):
            lock.acquire()
            tic1: float = time()
            acc: ACC = random.choice(self.accs)
            departure_airport: str = random.choice(acc.atcs).airport
            while True:
                arrival_airport: str = random.choice(acc.atcs).airport
                if arrival_airport != departure_airport:
                    break
            tic2 = time()
            admission_time: int = random.randint(0, 30 * self.num_flights)  # 0 - 30 * num_flights
            # need empirical analysis to determine op_times distribution
            op_times: list[int] = [random.randint(0, 500) for _ in range(21)]  # 21 op_times per flight
            tic3 = time()
            flight_code: str = self.flight_codes.pop()
            tic4 = time()
            self.flights.append(Flight(admission_time, flight_code, acc.code,
                                       departure_airport, arrival_airport, op_times))
            tic5 = time()
            if _ % 100 == 0 and False:
                # round to 4 decimal places
                print("")
                print(f"Flight {flight_code} created in {(tic5 - tic1) * 1000:.4f} ms")
                print(f"Time to choose acc: {(tic2 - tic1) * 1000:.4f} ms")
                print(f"Time to choose airports: {(tic3 - tic2) * 1000:.4f} ms")
                print(f"Time to choose flight code: {(tic4 - tic3) * 1000:.4f} ms")
                print(f"Time to create flight: {(tic5 - tic4) * 1000:.4f} ms")
                print("")

            lock.release()
        return

    def choose_accs(self) -> list[ACC]:
        # load acc codes from data/acc_codes.json
        acc_codes: list[str] = list(json.loads(open("data/acc_codes.json", "r").read()))
        # choose random acc codes and create acc objects
        return [ACC(code) for code in random.sample(acc_codes, self.num_accs)]

    # 1. First line: <number of ACCs> <number of flights> 2. Next <number of ACCs> lines: <ACC code> <airport code>
    # <airport code> ... <airport code> 3. Next <number of flights> lines: <admission time> <flight code> <ACC code>
    # <departure airport code> <arrival airport code> <list of operation times>
    def __repr__(self):
        return f"{self.num_accs} {self.num_flights}\n" + "\n".join([str(acc) for acc in self.accs]) + "\n" + "\n".join(
            [str(flight) for flight in self.flights]) + "\n"


if __name__ == "__main__":
    inn = 18
    too = 1
    for i in range(inn, inn + too):
        print("generating input ", f"{DESTINATION_DIRECTORY}/{INPUT_NAME}{i}.{INPUT_EXTENSION}")
        file = open(f"{DESTINATION_DIRECTORY}/{INPUT_NAME}{i}.{INPUT_EXTENSION}", "w")
        inn = Input(1000, 1000000)
        file.write(str(inn))
        file.close()

    exit(0)
