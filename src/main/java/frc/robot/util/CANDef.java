package frc.robot.util;

public class CANDef {
  private final int id;
  private final CANBus bus;

  public enum CANBus {
    Rio("rio"),
    CANivore("ARM");

    private final String name;

    private CANBus(String name) {
      this.name = name;
    }
  }

  private CANDef(int id, CANBus bus) {
    if (id < 0) {
      throw new IllegalArgumentException(
          "CAN ID (" + id + ", " + bus.name + ") must be non-negative");
    }

    this.id = id;
    this.bus = bus;
  }

  public int id() {
    return id;
  }

  public String bus() {
    return bus.name;
  }

  public static Builder builder() {
    return new Builder();
  }

  // create a builder for this class
  public static class Builder {
    private int id = -1;
    private CANBus bus = CANBus.Rio;

    public Builder id(int id) {
      this.id = id;
      return this;
    }

    public Builder bus(CANBus bus) {
      this.bus = bus;
      return this;
    }

    public CANDef build() {
      return new CANDef(id, bus);
    }
  }
}
