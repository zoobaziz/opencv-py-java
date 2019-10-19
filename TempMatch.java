import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.util.ArrayList;

public class TempMatch {

    private String videoFilePath = ".\\Video\\triple alerts.mp4";

    public static void main(String[] args){
        System.out.println("start");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ArrayList<Double> timestamp = new ArrayList<>();

        Mat source=null;
        Mat template=null;
        String filePath="C:\\Users\\abhishekj13\\IdeaProjects\\TemplMatch\\Templates\\";
        //Load image file
        source=Imgcodecs.imread(filePath+"Frame_Ref268.png");
        template=Imgcodecs.imread(filePath+"Frame_Ref4.png");
        vidMatch("C:\\Users\\abhishekj13\\PycharmProjects\\demo\\Frames", source/*"C:\\Users\\abhishekj13\\IdeaProjects\\TemplMatch\\Templates\\Frame_Ref4.png"*/, timestamp);
//        double res = matchTemplateByRect(source, template,1008 ,692 , 496, 303,90.0);
        System.out.println(timestamp.size());
        System.out.println();
        for (double timestmp: timestamp) {
            System.out.println("Timestamp: "+(timestmp/1000));
        }
//        System.out.println(res);
        System.out.println("stop");
    }

    public static void vidMatch(String videoFilePath, Mat template, ArrayList<Double> timestamp)
    {
        Mat frame = new Mat();
        VideoCapture camera = new VideoCapture(videoFilePath);
        camera.open(videoFilePath);
        if (!camera.isOpened()) {
            System.out.println("video not opened");
        }
        while(camera.read(frame)){
            double res = matchTemplateByRect(frame, template,1008 ,692 , 496, 303,90.0);
            if(res > 95)
            {
                timestamp.add(camera.get(Videoio.CAP_PROP_POS_MSEC));
            }
        }
    }


    public static double matchTemplateByRect(Mat sourceImg, Mat referenceImg, int xPosition, int yPosition,
                                             int width, int height, double matchingPercentile) {
        Mat outputImage = new Mat();

        if(sourceImg.rows()==referenceImg.rows() && sourceImg.cols()==referenceImg.cols())
        {
            // Crop image to create and match template
            Rect roi = new Rect(xPosition, yPosition, width, height);

            // Mat sourceRect = sourceImg.submat(roi);
            Mat templateImg = referenceImg.submat(roi);


            // Define method for template matching
            int machMethod = Imgproc.TM_CCOEFF_NORMED;

            // Template matching method
            Imgproc.matchTemplate(sourceImg, templateImg, outputImage, machMethod);

            // Select best matches more than 0.8 to 1.0
            Imgproc.threshold(outputImage, outputImage, 0.8, 1.0, Imgproc.THRESH_TOZERO);

            // Calculate points
            Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
            Point matchLoc = mmr.maxLoc;
            double actualMatchingValue = mmr.maxVal * 100;
            if (actualMatchingValue >= matchingPercentile && (matchLoc.x >= (xPosition - 100) && matchLoc.x <= (xPosition + width + 100))
                    && (matchLoc.y >= (yPosition - 100) && matchLoc.y <= (yPosition + height + 100))) {
                return actualMatchingValue;
            } else {
                return 0;
            }
        }
        return 0;
    }



    public static double tempMatch(Mat main_image, Mat template)
    {


        int machMethod = Imgproc.TM_CCOEFF_NORMED;
        Mat outputImage= new Mat();
        /*double gammaValue = 0.5;
        Mat lut = new Mat(1, 256, CvType.CV_8U);
        byte[] lutdata = new byte[(int) (lut.total()*lut.channels())];
        for(int i=0; i<lut.cols(); i++)
        {
            lutdata[i] = saturate(Math.pow(i/255.0, gammaValue) * 255.0);
        }
        lut.put(0,0, lutdata);
        Core.LUT(main_image);
*/
//        Imgproc.cvtColor(main_image, main_image, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.cvtColor(template, template, Imgproc.COLOR_BGR2GRAY);
        // Template matching method
        Imgproc.matchTemplate(main_image, template, outputImage, machMethod);


        // Select best matches more than 0.8 to 1.0
        //Imgproc.threshold(outputImage, outputImage, 0.8, 1.0, Imgproc.THRESH_TOZERO);

        // Calculate points
        Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        Point matchLoc = mmr.maxLoc;
        double actualMatchingValue = Math.round(mmr.maxVal * 100);
        //System.out.println(actualMatchingValue);

        if(actualMatchingValue>95.0){
            return actualMatchingValue;
        }
        else {
            return 0;
        }
    }

    private static byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }
   /* private void matcher()
    {
        Mat frame = new Mat();
        int frameCount = 0;
        String fileName = null;
        VideoCapture camera = new VideoCapture(videoFilePath);
        camera.open(videoFilePath);
        if (!camera.isOpened()) {

        }

        while (camera.read(frame)) {
            frameCount++;
            if (width == 0 && height == 0) {
                setWidth(frame.width());
                setHeight(frame.height());
            }
            // Convert matrix to buffered image
            BufferedImage bufferedImage = matrixToBufferedImage(frame);
            fileName = newFolderWithTimeStamp + System.getProperty("file.separator") + initialNameOfVideo + frameCount
                    + ".png";
            try {
                ImageIO.write(bufferedImage, "png", new File(fileName));
                //EventNotification.displayNotification(EventNotification.INFO,SPLITVIDEO);
                // TODO : Send event notification on UI for this
            } catch (IOException e) {
                // TODO Auto-generated catch block
                CoreLogger.error(this.getClass(), LoggerPrefixConstants.PLUGIN_IMAGECOMP_PREFIX, e.getMessage());
            }
    }*/

}

/* Mat source=null;
        Mat template=null;
        String filePath="C:\\Users\\abhishekj13\\IdeaProjects\\TemplMatch\\Templates\\";
        //Load image file
        source=Imgcodecs.imread(filePath+"StartupRef177.png");
        template=Imgcodecs.imread(filePath+"collisionalert.png");


        Mat outputImage=new Mat();
        int machMethod=Imgproc.TM_CCOEFF;
        //Template matching method
        Imgproc.matchTemplate(source, template, outputImage, Imgproc.TM_CCOEFF_NORMED);


        Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        Point matchLoc=mmr.maxLoc;
        System.out.println(mmr.maxVal);
        //Draw rectangle on result image
        Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + template.cols(),
                matchLoc.y + template.rows()), new Scalar(255, 255, 255), 2);

        Imgcodecs.imwrite(filePath+"sonuc.jpg", source);
        System.out.println("Completed.");*/