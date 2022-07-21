package com.ple.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleCode {

    public static class Person {
        private String firstName;
        private String lastName;
        private int old;

        public Person(String firstName, String lastName, int old) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.old = old;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public int getOld() {
            return old;
        }

        public void logInfo(ILog log) {
            String info = log.doLog(this);
            System.out.println("Person: " + info);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", old=" + old +
                    '}';
        }
    }

    interface ILog {
        String doLog(Person person);
    }


    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("phuong", "le", 32));
        list.add(new Person("phuong", "bui", 32));
        list.add(new Person("khai", "bui", 3));

        Comparator<Person> reversed =
                Comparator.comparing(Person::getOld, Integer::compareTo)
                .thenComparing(Person::getLastName, String::compareTo);
        Collections.sort(list, reversed);

        System.out.println("list1: ");
        list.stream().forEach(person -> person.logInfo(person1 -> person1.firstName + " " + person1.lastName + " - " + person1.old));

        System.out.println("list2: ");
        list.stream().forEach(person -> person.logInfo(person1 -> "name: " + person1.firstName + " " + person1.lastName + ", old: " + person1.old));

    }
}
