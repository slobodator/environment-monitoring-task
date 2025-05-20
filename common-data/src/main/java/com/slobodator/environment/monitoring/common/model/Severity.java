package com.slobodator.environment.monitoring.common.model;

import java.util.Comparator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// the order doesn't matter, they are sorted by index
public enum Severity {
  OK(0),
  CRITICAL(2),
  WARNING(1);

  final int index;

  public enum IndexComparator implements Comparator<Severity> {
    INSTANCE;

    @Override
    public int compare(Severity o1, Severity o2) {
      return o1.index - o2.index;
    }
  }
}
