//
// Created by Alex Q on 3/4/18.
//

#include "FrequencyCount.h"
#include <iostream>
#include <vector>
#include <map>

/**
 * Returns and removes the element of the passed in map that has the highest value associated with any key
 * @param map the map to search through
 * @return a copy of the element with the highest value
 */
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

int main(int argc, char *argv[]) {
	// Check the number of parameters. This if statement is derived from http://www.cplusplus.com/articles/DEN36Up4/

	if (argc == 0) {
		// Tell the user how to run the program
		std::cerr << "Usage: " << argv[0] << " FILE NAME" << std::endl;
		return 1;
	}

	// Declare intermediate variables, and read input from console command
	std::vector <std::string> words = std::vector<std::string>();
	std::string doc_text;
	std::cin >> doc_text;

	// Removes all unwanted characters from each element of the words vector
	do {
		// Sets an unsigned integer to the index of the first instance of the unwanted character
		size_t unwanted_char = doc_text.find_first_of("/!?,.:\n\t\r");

		// While an unwanted char still exists in the word
		while (unwanted_char != std::string::npos) {
			doc_text.erase(unwanted_char, 1);
			unwanted_char = doc_text.find_first_of("/!?,.:\n\t\r");
		}

		// Don't add the word if everything was removed
		if (doc_text.size() > 0)
			words.push_back(doc_text);

	} while (std::cin >> doc_text);

	std::map<std::string, int> word_frequencies;

	// Adds everything in the words vector to the map of word frequencies. Duplicate words increment their pre-existing associated values.
	for (int i = 0; i < words.size(); i++) {
		if (word_frequencies.count(words.at(i)) == 0) {
			word_frequencies.insert(std::pair<std::string, int>(words.at(i), 1));
		} else {
			word_frequencies[words.at(i)]++;
		}
	}
	const int kMaxWordsToPrint = 10;

	// Sets the number of words to print, up to 10 depending on the number of words in the file
	int words_to_print;
	if (word_frequencies.size() >= 10){
		words_to_print = kMaxWordsToPrint;
	} else {
		words_to_print = word_frequencies.size();
	}

	// Uses the get_max_entry method to print and remove the 10 words with the highest associated values
	std::cout << "Most common words:" << std::endl;
	for (int i = 0; i < words_to_print; i++) {
		std::cout << get_max_entry(word_frequencies).first << std::endl;
	}
}
