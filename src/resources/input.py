import json
import random

DESTINATION_DIRECTORY = "inputs"
INPUT_NAME = "input"
INPUT_EXTENSION = "in"


class Airport:
    def __init__(self, code: str) -> None:
        self.code: str = code
        self.connections: set[Airport] = set()  # undirected graph

    def __repr__(self) -> str:
        return "".join([self.code, " -> ", str(self.connections)])


class Input:
    def __init__(self, airport_count: int, flight_count: int, mode: str = "single") -> None:
        self.airport_count = airport_count
        self.flight_count = flight_count
        self.__set_airports__(mode)
        self.__set_flights__(mode)
        self.mode = mode

    def create_airport(self) -> Airport:
        airport_code: str = random.choice(list(self.airport_codes))
        self.airport_codes.remove(airport_code)
        return Airport(airport_code)

    """
        airport code : str (3 letters)
        connections : list of tuples (airport code, distance)
    """

    def __set_airports__(self, mode: str):
        # read airport codes from file json file named data/airport_codes.json
        self.airport_codes = set(json.loads(open("data/airport_codes.json", "r").read()))
        # create airport objects
        self.airports: list[Airport] = [self.create_airport() for _ in range(self.airport_count)]
        self.airports_dict: dict[str, Airport] = {airport.code: airport for airport in self.airports}
        # connect airports based on mode. default is single
        switcher = {
            "random": self.connect_airports_random,
            "single_cycle": self.connect_single_cycle,
            "single_component": self.connect_single_component,
            "sparse": self.connect_airports_loose,
            "dense": self.connect_airports_tight,
        }
        switcher[mode]()
        return self.airports

    def connect_airports_random(self) -> list[Airport]:
        airport: Airport
        for airport in self.airports:
            # airport.connections = set(random.sample(self.airports, random.randint(1, self.airport_count - 1)))
            for _ in range(random.randint(1, self.airport_count - 1)):
                random_airport = random.choice(self.airports)
                if random_airport != airport:
                    airport.connections.add(random_airport)
                    random_airport.connections.add(airport)
        return self.airports

    def connect_single_cycle(self):
        pass

    def connect_single_component(self):
        pass

    """
    # connected airports form a single connected component with one cycle
    def connect_single_cycle(self, airports: list[Airport]) -> list[Airport]:
        for airport_index in range(len(airports) - 1):
            airports[airport_index].connections.add(airports[airport_index + 1])
            airports[airport_index + 1].connections.add(airports[airport_index])
        airports[0].connections.add(airports[-1])
        airports[-1].connections.add(airports[0])
        return airports

    # connected airports form a single connected component without a cycle
    def connect_single_component(self, airports: list[Airport]) -> list[Airport]:
        for airport_index in range(len(airports) - 1):
            airports[airport_index].connections.add(airports[airport_index + 1])
            airports[airport_index + 1].connections.add(airports[airport_index])
        airport_index = 1
        while airport_index < self.airport_count - 1:
            airports[airport_index].connections.add(airports[airport_index + 1])
            airports[airport_index + 1].connections.add(airports[airport_index])
            airport_index *= airport_index
        airport_index = self.airport_count - 2
        while airport_index > 1:
            airports[airport_index].connections.add(airports[airport_index + 1])
            airports[airport_index + 1].connections.add(airports[airport_index])
            airport_index //= 2
        return airports
    """

    # airports = comp * airport_in_comp
    def connect_airports_loose(self) -> list[Airport]:
        component_count: int = (self.airport_count // 20) + 1
        for _ in range(component_count):
            pass
        return self.airports

    def connect_airports_tight(self) -> list[Airport]:
        component_count: int = (self.airport_count // 100) + 1
        for _ in range(component_count):
            pass
        return self.airports

    def connect_airports_mixed(self) -> list[Airport]:
        split_index: int = 3
        while split_index < self.airport_count - 1:

            split_index *= split_index

        return self.airports

    def __set_flights__(self, mode: str):
        pass

    def __repr__(self):
        return ""


if __name__ == "__main__":
    for i in range(10):
        file = open(f"{DESTINATION_DIRECTORY}/{INPUT_NAME}{i}.{INPUT_EXTENSION}", "w")
        inn = Input(10, 10)
        file.write(str(inn))
        file.close()

    exit(0)
