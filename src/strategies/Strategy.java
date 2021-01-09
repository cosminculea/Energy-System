package strategies;

import entities.Producer;

import java.util.List;

public interface Strategy {
    List<Producer> applyStrategy();
    String getType();
}
