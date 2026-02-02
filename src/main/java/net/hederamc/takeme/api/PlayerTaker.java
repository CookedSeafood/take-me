package net.hederamc.takeme.api;

public interface PlayerTaker {
    default int getLastTakePlayerTimestamp() {
        throw new UnsupportedOperationException();
    }

    default void setLastTakePlayerTimestamp(int timestamp) {
        throw new UnsupportedOperationException();
    }
}
