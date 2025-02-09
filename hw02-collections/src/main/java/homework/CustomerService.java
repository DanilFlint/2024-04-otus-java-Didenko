package homework;

import java.util.*;

public class CustomerService {

    NavigableMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    // todo: 3. надо реализовать методы этого класса
    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        var smallest = map.firstEntry();
        try {
            return new AbstractMap.SimpleEntry<>((Customer) smallest.getKey().clone(), smallest.getValue());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var next = map.tailMap(customer, false).firstEntry();
        if (next == null) return next;
        try {
            return new AbstractMap.SimpleEntry<>((Customer) next.getKey().clone(), next.getValue());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
