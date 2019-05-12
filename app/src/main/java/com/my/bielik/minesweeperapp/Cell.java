package com.my.bielik.minesweeperapp;

public class Cell {
    /** Состояние клетки (пустая клетка / клетка с миной / взорванная клетка) */
    private int state;

    /** Указывает на то, открыта ли клетка пользователем */
    private boolean isHidden;

    /** Указывает на то, была ли клетка отмечена пользователем (поставил ли он на неё флажок) */
    private boolean isMarked;

    /**
     * Инициализирует поля класса
     *
     * @param isMine Является ли клетка миной
     */
    public Cell(boolean isMine){
        this.isHidden = true;
        this.isMarked = false;
        this.state = isMine ? CellState.MINE : CellState.EMPTY;
    }

    /**
     * @return Возвращает информацию о том, была ли клетка открыта пользователем
     */
    public boolean isHidden(){
        return isHidden;
    }

    /**
     * @return Возвращает информацию о том, была ли клетка помечена пользователем (поставил ли он на неё флажок)
     */
    public boolean isMarked(){
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    /**
     * @return Возвращает сотостояние клетки (пустая клетка / клетка с миной / взорванная клетка)
     */
    public int getState() {
        return state;
    }

    /**
     * Открывает содержимое клетки
     */
    public void show() {
        this.isHidden = false;
    }

    public class CellState {
        public static final int EMPTY = 0;
        public static final int MINE = 1;
        public static final int EXPLOSED = 2;
    }
}
