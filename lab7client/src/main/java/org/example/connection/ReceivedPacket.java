package org.example.connection;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ReceivedPacket {
    @Setter
    private int sizeOfResponse = Integer.MAX_VALUE;
    private final List<ImmutablePair<byte[],Integer>> packets = Collections.synchronizedList(new ArrayList<ImmutablePair<byte[],Integer>>());


}
