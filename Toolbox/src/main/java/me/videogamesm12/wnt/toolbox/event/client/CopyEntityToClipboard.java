package me.videogamesm12.wnt.toolbox.event.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.videogamesm12.wnt.event.CustomEvent;
import me.videogamesm12.wnt.toolbox.data.QueriedEntityDataSet;

@AllArgsConstructor
@Getter
public class CopyEntityToClipboard extends CustomEvent
{
    private QueriedEntityDataSet dataSet;
}