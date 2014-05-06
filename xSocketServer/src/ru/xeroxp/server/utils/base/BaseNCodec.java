/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.xeroxp.server.utils.base;

import java.util.Arrays;

public abstract class BaseNCodec implements BinaryEncoder, BinaryDecoder {

    public static final int PEM_CHUNK_SIZE = 64;
    static final int MIME_CHUNK_SIZE = 76;
    static final int MASK_8BITS = 0xff;
    static final byte PAD_DEFAULT = '=';
    final byte PAD = PAD_DEFAULT;
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    final int lineLength;
    private final int unencodedBlockSize;

    private final int encodedBlockSize;
    private final int chunkSeparatorLength;

    BaseNCodec(final int unencodedBlockSize, final int encodedBlockSize,
               final int lineLength, final int chunkSeparatorLength) {
        this.unencodedBlockSize = unencodedBlockSize;
        this.encodedBlockSize = encodedBlockSize;
        final boolean useChunking = lineLength > 0 && chunkSeparatorLength > 0;
        this.lineLength = useChunking ? (lineLength / encodedBlockSize) * encodedBlockSize : 0;
        this.chunkSeparatorLength = chunkSeparatorLength;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean isWhiteSpace(final byte byteToCheck) {
        switch (byteToCheck) {
            case ' ':
            case '\n':
            case '\r':
            case '\t':
                return true;
            default:
                return false;
        }
    }

    boolean hasData(final Context context) {
        return context.buffer != null;
    }

    int available(final Context context) {
        return context.buffer != null ? context.pos - context.readPos : 0;
    }

    int getDefaultBufferSize() {
        return DEFAULT_BUFFER_SIZE;
    }

    private byte[] resizeBuffer(final Context context) {
        if (context.buffer == null) {
            context.buffer = new byte[getDefaultBufferSize()];
            context.pos = 0;
            context.readPos = 0;
        } else {
            final byte[] b = new byte[context.buffer.length * DEFAULT_BUFFER_RESIZE_FACTOR];
            System.arraycopy(context.buffer, 0, b, 0, context.buffer.length);
            context.buffer = b;
        }
        return context.buffer;
    }

    byte[] ensureBufferSize(final int size, final Context context) {
        if ((context.buffer == null) || (context.buffer.length < context.pos + size)) {
            return resizeBuffer(context);
        }
        return context.buffer;
    }

    void readResults(final byte[] b, final int bPos, final int bAvail, final Context context) {
        if (context.buffer != null) {
            final int len = Math.min(available(context), bAvail);
            System.arraycopy(context.buffer, context.readPos, b, bPos, len);
            context.readPos += len;
            if (context.readPos >= context.pos) {
                context.buffer = null; // so hasData() will return false, and this method can return -1
            }
            //return len;
        }
        //return context.eof ? EOF : 0;
    }

    @Override
    public Object encode(final Object obj) throws EncoderException {
        if (!(obj instanceof byte[])) {
            throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
        }
        return encode((byte[]) obj);
    }

    public String encodeToString(final byte[] pArray) {
        return Base64.newStringUtf8(encode(pArray));
    }

    public String encodeAsString(final byte[] pArray) {
        return Base64.newStringUtf8(encode(pArray));
    }

    @Override
    public Object decode(final Object obj) throws DecoderException {
        if (obj instanceof byte[]) {
            return decode((byte[]) obj);
        } else if (obj instanceof String) {
            return decode((String) obj);
        } else {
            throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
        }
    }

    byte[] decode(final String pArray) {
        return decode(Base64.getBytesUtf8(pArray));
    }

    @Override
    public byte[] decode(final byte[] pArray) {
        if (pArray == null || pArray.length == 0) {
            return pArray;
        }
        final Context context = new Context();
        decode(pArray, 0, pArray.length, context);
        decode(pArray, 0, EOF, context); // Notify decoder of EOF.
        final byte[] result = new byte[context.pos];
        readResults(result, 0, result.length, context);
        return result;
    }

    @Override
    public byte[] encode(final byte[] pArray) {
        if (pArray == null || pArray.length == 0) {
            return pArray;
        }
        final Context context = new Context();
        encode(pArray, 0, pArray.length, context);
        encode(pArray, 0, EOF, context); // Notify encoder of EOF.
        final byte[] buf = new byte[context.pos - context.readPos];
        readResults(buf, 0, buf.length, context);
        return buf;
    }

    abstract void encode(byte[] pArray, int i, int length, Context context);

    abstract void decode(byte[] pArray, int i, int length, Context context);

    protected abstract boolean isInAlphabet(byte value);

    boolean isInAlphabet(final byte[] arrayOctet, final boolean allowWSPad) {
        for (byte anArrayOctet : arrayOctet) {
            if (!isInAlphabet(anArrayOctet) &&
                    (!allowWSPad || (anArrayOctet != PAD) && !isWhiteSpace(anArrayOctet))) {
                return false;
            }
        }
        return true;
    }

    public boolean isInAlphabet(final String baseN) {
        return isInAlphabet(Base64.getBytesUtf8(baseN), true);
    }

    boolean containsAlphabetOrPad(final byte[] arrayOctet) {
        if (arrayOctet == null) {
            return false;
        }
        for (final byte element : arrayOctet) {
            if (PAD == element || isInAlphabet(element)) {
                return true;
            }
        }
        return false;
    }

    long getEncodedLength(final byte[] pArray) {
        // Calculate non-chunked size - rounded up to allow for padding
        // cast to long is needed to avoid possibility of overflow
        long len = ((pArray.length + unencodedBlockSize - 1) / unencodedBlockSize) * (long) encodedBlockSize;
        if (lineLength > 0) { // We're using chunking
            // Round up to nearest multiple
            len += ((len + lineLength - 1) / lineLength) * chunkSeparatorLength;
        }
        return len;
    }

    static class Context {

        int ibitWorkArea;

        long lbitWorkArea;

        byte[] buffer;

        int pos;

        int readPos;

        boolean eof;

        int currentLinePos;

        int modulus;

        Context() {
        }

        @SuppressWarnings("boxing") // OK to ignore boxing here
        @Override
        public String toString() {
            return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, " +
                            "modulus=%s, pos=%s, readPos=%s]", this.getClass().getSimpleName(), Arrays.toString(buffer),
                    currentLinePos, eof, ibitWorkArea, lbitWorkArea, modulus, pos, readPos
            );
        }
    }

}
