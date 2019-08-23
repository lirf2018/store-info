package com.yufan.utils;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * 功能名称: 图片处理类
 * 开发人: lirf
 * 开发时间: 2015下午9:37:58
 * 其它说明：
 */
public class ImageUtil {

    private Logger LOG = org.apache.log4j.Logger.getLogger(ImageUtil.class);
    private volatile static ImageUtil imageUtil;

    public static ImageUtil getInstance() {
        if (null == imageUtil) {
            synchronized (ImageUtil.class) {
                if (null == imageUtil) {
                    imageUtil = new ImageUtil();
                }
            }
        }
        return imageUtil;
    }

    /**
     * 根据byte数组，生成文件
     *
     * @param bfile
     * @param filePath
     * @param fileName
     */
    public boolean getFile(byte[] bfile, String filePath, String fileName) {
        try {
            BufferedOutputStream bos = null;
            FileOutputStream fos = null;
            File file = null;
            try {
                File dir = new File(filePath);
                if (!dir.exists() && !dir.isDirectory()) {// 判断文件目录是否存在
                    dir.mkdirs();
                }
                file = new File(filePath + "/" + fileName);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(bfile);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 根据 InputStream 数组，生成文件
     *
     * @param in
     * @param filePath
     * @param fileName
     */
    public void getFile(InputStream in, String filePath, String fileName) {
        try {
            BufferedOutputStream bos = null;
            FileOutputStream fos = null;
            File file = null;
            try {
                File dir = new File(filePath);
                if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                    dir.mkdirs();
                }
                file = new File(filePath + "/" + fileName);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                byte[] buffer = new byte[1024];
                int len = 0;
                // 从数据库中读取到指定的字节数组中
                while ((len = in.read(buffer)) != -1) {
                    // 从指定的数组中读取，然后输出来，
                    // 所以这里buffer好象是连接inputStream和outputStream的一个东西
                    bos.write(buffer, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获得指定文件的byte数组
    public byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                // delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    // //删除文件夹
    // //param folderPath 文件夹完整绝对路径
    // public   void delFolder(String folderPath) {
    // try {
    // delAllFile(folderPath); //删除完里面所有内容
    // String filePath = folderPath;
    // filePath = filePath.toString();
    // java.io.File myFilePath = new java.io.File(filePath);
    // myFilePath.delete(); //删除空文件夹
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * Blob写文件
     *
     * @param filePath
     * @param blob
     */
    static void getBlobToFile(String filePath, java.sql.Blob blob) {
        InputStream ins = null;
        OutputStream fout = null;
        try {
            ins = blob.getBinaryStream();
            //输出到文件
            File file = new File(filePath);
            fout = new FileOutputStream(file);
            //将BLOB数据写入文件
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = ins.read(b)) != -1) {
                fout.write(b, 0, len);
            }
            fout.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文件到指定文件夹
     *
     * @param filePath     文件路径+文件名
     * @param removeToPath 指定文件夹
     */
    public synchronized void removePathImg(String filePath, String removeToPath) {
        try {

            if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(removeToPath)) {
                return;
            }
            filePath = filePath.replace("/", "\\");
            removeToPath = removeToPath.replace("/", "\\");
            //文件名
            String fileNames[] = filePath.split("\\\\");
            String fileName = fileNames[fileNames.length - 1];
            LOG.info("删除的文件名称-->" + fileName);
            File deleteFile = new File(removeToPath);
            if (!deleteFile.exists()) {
                deleteFile.mkdirs();
            }
            File file = new File(filePath);
            File delFile = new File(removeToPath + "\\" + fileName);

            LOG.info("--->移动的文件路径: " + filePath);
            LOG.info("--->移动后的文件路径: " + removeToPath + "\\" + fileName);

            if (null == file || !file.exists()) {
                LOG.info("文件不存在-->" + file);
                return;
            }
            long fileSize = file.length();
            FileOutputStream outputStream = new FileOutputStream(delFile);
            //读或写的字节数
            long writeByte = Files.copy(file.toPath(), outputStream);
            if (writeByte == fileSize) {
                file.delete();
            } else {
                LOG.info("--->删除失败--->path=" + filePath);
            }
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean removePathImg(String filePath, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(filePath) || null == request) {
                return false;
            }
            //移动图片
            String path = request.getSession().getServletContext().getRealPath("/");
            String p[] = path.split("webapps");
            String removeToPath = p[0] + "webapps/image/delImg";
            filePath = p[0] + "webapps/image/" + filePath;

            filePath = filePath.replace("/", "\\");
            removeToPath = removeToPath.replace("/", "\\");
            //文件名
            String fileNames[] = filePath.split("\\\\");
            String fileName = fileNames[fileNames.length - 1];
            LOG.info("删除的文件名称-->" + fileName);
            File deleteFile = new File(removeToPath);
            if (!deleteFile.exists()) {
                deleteFile.mkdirs();
            }
            File file = new File(filePath);
            File delFile = new File(removeToPath + "\\" + fileName);

            LOG.info("--->移动的文件路径: " + filePath);
            LOG.info("--->移动后的文件路径: " + removeToPath + "\\" + fileName);

            if (null == file || !file.exists()) {
                LOG.info("文件不存在-->" + file);
                return true;
            }
            long fileSize = file.length();
            FileOutputStream outputStream = new FileOutputStream(delFile);
            //读或写的字节数
            long writeByte = Files.copy(file.toPath(), outputStream);
            if (writeByte == fileSize) {
                file.delete();
            } else {
                LOG.info("--->删除失败--->path=" + filePath);
            }
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除图片
     *
     * @param filePath
     * @param request
     * @return
     */
    public synchronized boolean deletePathImg(String filePath, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(filePath) || null == request) {
                return false;
            }
            //删除图片
            String path = request.getSession().getServletContext().getRealPath("/");
            String p[] = path.split("webapps");
            String removeToPath = p[0] + "webapps/image/delImg";
            filePath = p[0] + "webapps/image/" + filePath;

            filePath = filePath.replace("/", "\\");
            removeToPath = removeToPath.replace("/", "\\");
            //文件名
            String fileNames[] = filePath.split("\\\\");
            String fileName = fileNames[fileNames.length - 1];
            LOG.info("删除的文件名称-->" + fileName);
            File deleteFile = new File(removeToPath);
            if (!deleteFile.exists()) {
                deleteFile.mkdirs();
            }
            File file = new File(filePath);
            File delFile = new File(removeToPath + "\\" + fileName);

            LOG.info("--->删除的文件路径: " + filePath);
            LOG.info("--->删除的文件路径: " + removeToPath + "\\" + fileName);

            if (file.exists()) {
                file.delete();
            }
            if (delFile.exists()) {
                delFile.delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 重新生成按指定宽度和高度的图像
     *
     * @param bi
     *            源图像文件地址
     * @param result
     *            新的图像地址
     * @param _width
     *            设置新的图像宽度
     * @param _height
     *            设置新的图像高度
     */
    public static void scale(BufferedImage bi, String result, int _width, int _height, int x, int y) {
        try {
            BufferedImage src = bi; // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            if (width > _width) {
                width = _width;
            }
            if (height > _height) {
                height = _height;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, x, y, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割（改） *
     *
     * @param srcImageFile
     *            源图像地址
     * @param dirImageFile
     *            新图像地址
     * @param x
     *            目标切片起点x坐标
     * @param y
     *            目标切片起点y坐标
     * @param destWidth
     *            目标切片宽度
     * @param destHeight
     *            目标切片高度
     */
    public static void abscut(String srcImageFile, String dirImageFile, int x, int y, int destWidth, int destHeight) {
        try {
            System.out.println("------->srcImageFile="+srcImageFile);
            System.out.println("------->dirImageFile="+dirImageFile);
            System.out.println("------->x="+x+"------->y="+y+"------->destWidth="+destWidth+"------->destHeight="+destHeight);
            Image img;
            ImageFilter cropFilter;
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getWidth(); // 源图宽度
            int srcHeight = bi.getHeight(); // 源图高度
            if (srcWidth >= destWidth && srcHeight >= destHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                /**
                 * 改进的想法:是否可用多线程加快切割速度,四个参数分别为图像起点坐标和宽高,
                 * 即: CropImageFilter(int x,int y,int width,int height)
                 */
                cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
                img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", new File(dirImageFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 根据byte数组，生成文件
     * @param bfile
     * @param filePath
     * @param fileName
     */
    public static void byteToFile(byte[] bfile, String filePath, String fileName) {
        try {
            BufferedOutputStream bos = null;
            FileOutputStream fos = null;
            File file = null;
            try {
                File dir = new File(filePath);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                    dir.mkdirs();
                }
                file = new File(filePath + "/" + fileName);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(bfile);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获得指定文件的byte数组
    public static  byte[] getBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 删除文件夹下所有文件
     */
//	public static  boolean delAllFile1(String path){
//		boolean flag = false;
//		File file = new File(path);
//		if (!file.exists()) {
//			return flag;
//		}
//		if (!file.isDirectory()) {
//			return flag;
//		}
//		String[] tempList = file.list();
//		File temp = null;
//		for (int i = 0; i < tempList.length; i++) {
//			if (path.endsWith(File.separator)) {
//				temp = new File(path + tempList[i]);
//			} else {
//				temp = new File(path + File.separator + tempList[i]);
//			}
//			if (temp.isFile()) {
//				temp.delete();
//			}
//			if (temp.isDirectory()) {
//				delAllFile1(path + "/" + tempList[i]);// 先删除文件夹里面的文件
//				// delFolder(path + "/" + tempList[i]);//再删除空文件夹
//				flag = true;
//			}
//		}
//		return flag;
//	}

    /**
     * 删除图片
     */
    public static  boolean delAllFile(String path,List<String> list){
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        if(null==list||list.size()==0){
            return flag;
        }
        File temp = null;
        for (int i = 0; i < list.size(); i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + list.get(i));
            } else {
                temp = new File(path + File.separator + list.get(i));
            }
            if (temp.isFile()) {
                temp.delete();
            }
        }
        return flag;
    }

}
