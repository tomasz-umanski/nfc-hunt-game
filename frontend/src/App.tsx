import {Toaster} from 'react-hot-toast';
import AppRouter from "@/routes/AppRouter.tsx";
import {useEffect} from "react";
import {initAuth} from "@/utils/authUtils.ts";

function App() {

    useEffect(() => {
        initAuth();
    }, []);

    return (
        <>
            <AppRouter/>
            <Toaster position="top-center"/>
        </>
    );
}

export default App;
