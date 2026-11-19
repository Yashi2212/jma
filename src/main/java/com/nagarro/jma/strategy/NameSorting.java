package com.nagarro.jma.strategy;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.nagarro.jma.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;



	public class NameSorting implements SortStrategy {

		@Override
		public List<User> sort(List<User> list, String sortBy) {
			int j=0;
			switch(sortBy) {
			case "even":
				for(int i=0;i<list.size();i++) {
					if(list.get(j).getName().length()%2!=0) {
						list.add(list.remove(j));
					}
					else {
						j++;
					}
					
				}
				break;
			case "odd":
				for(int i=0;i<list.size();i++) {
					if(list.get(j).getName().length()%2==0) {
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
