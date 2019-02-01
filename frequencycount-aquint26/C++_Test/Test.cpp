#define CATCH_CONFIG_MAIN
#include "catch.hpp"
#include <map>

std::pair<std::string, int> get_max_entry(std::map<std::string, int>& map) {
	int max_val = 0;
	std::pair<std::string, int> max_entry("", 0);

	// searches for the element with the highest value
	for (std::map<std::string, int>::iterator iter = map.begin();
		 iter != map.end();
		 iter++) {

		if ((*iter).second > max_val) {
			max_entry = std::pair<std::string, int>((*iter).first, (*iter).second);
			max_val = (*iter).second;
		}
	}

	// If something was found, return the max_entry variable
	if (max_entry.second != 0) {
		map.erase(max_entry.first);
		return max_entry;
	}

	// If nothing was found, return this instead (should never happen)
	return std::pair<std::string, int> ("error", 0);
}

TEST_CASE("Test the max value function"){
	std::map<std::string, int> test_map;
	test_map.insert(std::pair<std::string, int>("one", 1));
	test_map.insert(std::pair<std::string, int>("two", 3));
	test_map.insert(std::pair<std::string, int>("three", 2));
	REQUIRE(get_max_entry(test_map).second == 3 );
	REQUIRE(get_max_entry(test_map).second == 2 );
	REQUIRE(get_max_entry(test_map).second == 1 );
}
