package top.thinkin.lightd.db;

import org.rocksdb.RocksIterator;
import top.thinkin.lightd.kit.BytesUtil;

public class RIterator<R extends RCollection> implements AutoCloseable {
    private final RocksIterator iterator;
    private final RCollection rCollection;
    private final byte[] seekHead;
    private byte[] next;
    private  boolean finish = false;

    public  RIterator(RocksIterator iterator,RCollection rCollection,byte[] seekHead){
        this.iterator = iterator;
        this.rCollection = rCollection;
        this.seekHead = seekHead;
    }

    public boolean hasNext(){
        if(finish) return false;
        byte[] key = iterator.key();
        if(key==null||!BytesUtil.checkHead(seekHead,key)){
            finish = true;
            return false;
        }
        return iterator.isValid();
    }


    public R.Entry next(){
        if (!iterator.isValid()) return null;
        R.Entry entry =  rCollection.getEntry(iterator);
        iterator.next();
        return entry;
    }

    public byte[] getSeek() {
        return seekHead;
    }

    @Override
    public void close() {
        if (iterator != null) {
            iterator.close();
        }
    }
}