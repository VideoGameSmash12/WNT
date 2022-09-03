package me.videogamesm12.wnt.toolbox.event.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.videogamesm12.wnt.event.CustomEvent;
import me.videogamesm12.wnt.toolbox.data.QueriedBlockDataSet;

@AllArgsConstructor
@Getter
public class CopyBlockToClipboard extends CustomEvent
{
    private QueriedBlockDataSet dataSet;
}