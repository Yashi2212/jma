package com.nagarro.jma.strategy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.nagarro.jma.model.User;

public class AgeSorting implements SortStrategy {
		   
		   
		   
		   @Override
			public List<User> sort(List<User> list, String sortBy) {
				int j=0;
				switch(sortBy) {
				case "even":
					for(int i=0;i<list.size();i++) {
						if(list.get(j).getAge() %2!=0) {
							list.add(list.remove(j));
						}
						else {
							j++;
						}
						
					}
					break;
				case "odd":
					for(int i=0;i<list.size();i++) {
						if(list.get(j).getAge() %2==0) {
							list.add(list.remove(j));
						}
						else {
							j++;
						}
					}
				
				break;
				
				default:
					System.out.println("Invalid Value");
				
			}
				return list;
			}

	}

 